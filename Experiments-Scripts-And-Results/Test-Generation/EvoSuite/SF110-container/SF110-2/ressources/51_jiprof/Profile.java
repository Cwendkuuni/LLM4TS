package com.mentorgen.tools.profile.runtime;

import com.mentorgen.tools.profile.*;
import java.util.*;
import com.mentorgen.tools.profile.output.*;
import java.io.*;

public final class Profile implements Runnable
{
    private static boolean _debugStart;
    private static boolean _debugException;
    private static ThreadDictionary _threadDictionary;
    private static List<Frame> _frameList;
    private static Map<Long, Frame> _threadActiveFrame;
    private static Map<String, Method> _methodDictionary;
    private static Object _lock;
    private static Map<String, ClassAllocation> _allocList;
    private static Controller _controller;
    private static Thread _controllerThread;
    
    public static void initProfiler() {
        System.err.println("Java Interactive Profiler: starting");
        init();
        Runtime.getRuntime().addShutdownHook(new Thread(new Profile()));
        Profile._controller = new Controller();
        if (Controller._remote) {
            (Profile._controllerThread = new Thread(Profile._controller)).start();
        }
    }
    
    public static void init() {
        Profile._threadActiveFrame = new HashMap<Long, Frame>(1001);
        Profile._threadDictionary = new ThreadDictionary();
        Profile._methodDictionary = new HashMap<String, Method>(2003);
        Profile._frameList = new ArrayList<Frame>(1001);
        Profile._lock = new Object();
        Profile._allocList = new HashMap<String, ClassAllocation>();
    }
    
    public static void clear() {
        init();
    }
    
    public static void start() {
        Profile._controller.start();
    }
    
    public static void stop() {
        Profile._controller.stop();
    }
    
    public static void setFileName(final String fileName) {
        Profile._controller.setFileName(fileName);
    }
    
    public static void shutdown() {
        synchronized (Profile._lock) {
            Controller._profile = false;
            for (final Long threadId : ((HashMap<Long, V>)Profile._threadDictionary).keySet()) {
                final Frame f = Profile._threadDictionary.getMostRecentFrame(threadId);
                f.close();
            }
            for (final Frame frame : frameList()) {
                frame.computeNetTime();
            }
        }
    }
    
    public static Iterable<Long> threads() {
        return Profile._threadDictionary.threads();
    }
    
    public static Iterable<Frame> interactions(final long threadId) {
        return Profile._threadDictionary.interactions(threadId);
    }
    
    public static Iterable<Frame> frameList() {
        return Profile._frameList;
    }
    
    public static Iterable<ClassAllocation> allocations() {
        return Profile._allocList.values();
    }
    
    public static long getThreadTotalTime(final long threadId) {
        return Profile._threadDictionary.getThreadTotalTime(threadId);
    }
    
    public static void sortFrameList(final Comparator<Frame> comp) {
        synchronized (Profile._lock) {
            Collections.sort(Profile._frameList, comp);
        }
    }
    
    public static void start(final String className, final String methodName) {
        final long start = System.nanoTime();
        final long threadId = Thread.currentThread().getId();
        synchronized (Profile._lock) {
            if (!Controller._profile) {
                return;
            }
            final Method method = new Method(className, methodName);
            if (Profile._methodDictionary.get(method.toString()) == null) {
                Profile._methodDictionary.put(method.toString(), method);
            }
            final Frame parent = Profile._threadActiveFrame.get(threadId);
            Frame target = null;
            if (parent != null) {
                target = parent.getChild(method);
                if (target == null) {
                    target = new Frame(parent, method, threadId);
                    Profile._frameList.add(target);
                }
            }
            else {
                target = new Frame(null, method, threadId);
                Profile._frameList.add(target);
                Profile._threadDictionary.add(threadId, target);
            }
            if (Profile._debugStart) {
                System.out.print("  (");
                System.out.print(className);
                System.out.print(" : ");
                System.out.print(methodName);
                System.out.println(')');
                final Frame root = Profile._threadDictionary.getMostRecentFrame(threadId);
                System.out.println(root);
            }
            Profile._threadActiveFrame.put(threadId, target);
            target.overhead(System.nanoTime() - start);
            target.setBeginTime(start);
        }
    }
    
    public static void end(final String className, final String method) {
        final long start = System.nanoTime();
        synchronized (Profile._lock) {
            final long threadId = Thread.currentThread().getId();
            final Frame target = findFrame(threadId, className, method);
            if (target == null) {
                return;
            }
            if (target.getParent() != null) {
                Profile._threadActiveFrame.put(threadId, target.getParent());
            }
            else {
                Profile._threadActiveFrame.put(threadId, null);
            }
            target.overhead(System.nanoTime() - start);
            target.setEndTime(System.nanoTime());
        }
    }
    
    public static void beginWait(final String className, final String methodName) {
        final long start = System.nanoTime();
        synchronized (Profile._lock) {
            final Frame target = findFrame(Thread.currentThread().getId(), className, methodName);
            if (target == null) {
                return;
            }
            target.overhead(System.nanoTime() - start);
            target.beginWait(System.nanoTime());
        }
    }
    
    public static void endWait(final String className, final String methodName) {
        final long start = System.nanoTime();
        synchronized (Profile._lock) {
            final Frame target = findFrame(Thread.currentThread().getId(), className, methodName);
            if (target == null) {
                return;
            }
            target.overhead(System.nanoTime() - start);
            target.endWait(System.nanoTime());
        }
    }
    
    public static void unwind(final String className, final String methodName, final String exception) {
        if (Profile._debugException || Controller._debug) {
            System.out.println("Catch: " + exception);
        }
        synchronized (Profile._lock) {
            final long threadId = Thread.currentThread().getId();
            final Frame target = findFrame(threadId, className, methodName);
            if (target == null) {
                return;
            }
            Profile._threadActiveFrame.put(threadId, target);
        }
    }
    
    private static final Frame findFrame(final long threadId, final String className, final String methodName) {
        if (!Controller._profile) {
            return null;
        }
        Frame target = Profile._threadActiveFrame.get(threadId);
        if (target == null) {
            return null;
        }
        boolean detectedException = false;
        while (!target.getClassName().equals(className) || !target.getMethodName().equals(methodName)) {
            if (!detectedException) {
                detectedException = true;
                if (Profile._debugException || Controller._debug) {
                    System.err.print("Detected an exception at ");
                    System.err.print(className);
                    System.err.print('.');
                    System.err.println(methodName);
                }
            }
            else if (Profile._debugException) {
                System.err.print("Unwinding ");
                System.err.print(target.getClassName());
                System.err.print('.');
                System.err.println(target.getMethodName());
            }
            target.setEndTime(System.nanoTime());
            target = target.getParent();
            if (target == null) {
                if (Profile._debugException) {
                    System.err.println("Stack completely unwound.");
                }
                return null;
            }
        }
        return target;
    }
    
    public static void alloc(final String className) {
        synchronized (Profile._lock) {
            if (!Controller._profile) {
                return;
            }
            final long threadId = Thread.currentThread().getId();
            final Frame target = Profile._threadActiveFrame.get(threadId);
            if (target != null && target.getClassName().equals(className) && target.getMethodName().equals("<init>")) {
                return;
            }
            ClassAllocation ca = Profile._allocList.get(className);
            if (ca == null) {
                ca = new ClassAllocation(className);
                Profile._allocList.put(className, ca);
            }
            ca.incAllocCount();
        }
    }
    
    @Override
    public void run() {
        try {
            if (Profile._threadDictionary.size() > 0) {
                shutdown();
                ProfileDump.dump();
            }
            Profile._controller.close();
            if (Profile._controllerThread != null) {
                Profile._controllerThread.interrupt();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static {
        Profile._debugStart = false;
        Profile._debugException = false;
    }
}
