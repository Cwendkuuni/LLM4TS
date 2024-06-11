package com.mentorgen.tools.profile.runtime;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        
        // Check if controller and controller thread are created
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
        assertTrue(Profile._controllerThread.isAlive());
    }

    @Test
    public void testInit() {
        Profile.init();

        // Check if all static variables are initialized
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.clear();

        // Check if the init() method is called
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testStart() {
        Profile._controller = new Controller();

        Profile.start();

        // Check if the start() method of the controller is called
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile._controller = new Controller();

        Profile.stop();

        // Check if the stop() method of the controller is called
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";

        Profile.setFileName(fileName);

        // Check if the fileName is set correctly
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        // Add some dummy data
        Profile._threadDictionary = new ThreadDictionary();
        Profile._threadDictionary.add(1L, new Frame(null, new Method("Class", "Method"), 1L));
        Profile._frameList = new ArrayList<Frame>();
        Profile._frameList.add(new Frame(null, new Method("Class", "Method"), 1L));
        Profile._threadActiveFrame = new HashMap<Long, Frame>();
        Profile._threadActiveFrame.put(1L, new Frame(null, new Method("Class", "Method"), 1L));

        Profile.shutdown();

        // Check if profile and netTime are computed correctly
        assertFalse(Controller._profile);
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._frameList.get(0).getNetTime());
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();

        // Check if threads are iterated correctly
        assertNotNull(threads);
        assertFalse(threads.iterator().hasNext());
    }

    @Test
    public void testInteractions() {
        long threadId = 1L;

        Iterable<Frame> interactions = Profile.interactions(threadId);

        // Check if interactions are iterated correctly
        assertNotNull(interactions);
        assertFalse(interactions.iterator().hasNext());
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();

        // Check if frameList is iterated correctly
        assertNotNull(frameList);
        assertFalse(frameList.iterator().hasNext());
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();

        // Check if allocations are iterated correctly
        assertNotNull(allocations);
        assertFalse(allocations.iterator().hasNext());
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadId = 1L;

        long totalTime = Profile.getThreadTotalTime(threadId);

        // Check if total time for the thread is obtained correctly
        assertEquals(0, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getBeginTime() - f2.getBeginTime();
            }
        };

        // Add some dummy data
        Profile._frameList = new ArrayList<Frame>();
        Frame frame1 = new Frame(null, new Method("Class1", "Method1"), 1L);
        frame1.setBeginTime(1L);
        Frame frame2 = new Frame(null, new Method("Class2", "Method2"), 2L);
        frame2.setBeginTime(2L);
        Profile._frameList.add(frame2);
        Profile._frameList.add(frame1);

        Profile.sortFrameList(comp);

        // Check if frameList is sorted correctly
        assertEquals(frame1, Profile._frameList.get(0));
        assertEquals(frame2, Profile._frameList.get(1));
    }

    @Test
    public void testStartMethod() {
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(className, methodName);

        // Check if a new frame is created and added correctly to the frameList
        assertEquals(1, Profile._frameList.size());
        assertEquals(className, Profile._frameList.get(0).getClassName());
        assertEquals(methodName, Profile._frameList.get(0).getMethodName());
    }

    @Test
    public void testEndMethod() {
        String className = "TestClass";
        String methodName = "testMethod";

        // Add some dummy data
        Profile._frameList = new ArrayList<Frame>();
        Frame frame = new Frame(null, new Method(className, methodName), 1L);
        Profile._frameList.add(frame);

        Profile.end(className, methodName);

        // Check if the end time is set correctly for the frame
        assertNotNull(frame.getEndTime());
    }

    @Test
    public void testBeginWait() {
        String className = "TestClass";
        String methodName = "testMethod";

        // Add some dummy data
        Profile._frameList = new ArrayList<Frame>();
        Frame frame = new Frame(null, new Method(className, methodName), 1L);
        Profile._frameList.add(frame);

        Profile.beginWait(className, methodName);

        // Check if the wait start time is set correctly for the frame
        assertNotNull(frame.getWaitStart());
    }

    @Test
    public void testEndWait() {
        String className = "TestClass";
        String methodName = "testMethod";

        // Add some dummy data
        Profile._frameList = new ArrayList<Frame>();
        Frame frame = new Frame(null, new Method(className, methodName), 1L);
        Profile._frameList.add(frame);

        Profile.endWait(className, methodName);

        // Check if the wait end time is set correctly for the frame
        assertNotNull(frame.getWaitEnd());
    }

    @Test
    public void testUnwind() {
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "NullPointer";

        Profile._debugException = true;
        Controller._debug = true;

        // Add some dummy data
        Profile._frameList = new ArrayList<Frame>();
        Frame frame = new Frame(null, new Method(className, methodName), 1L);
        Profile._frameList.add(frame);

        Profile.unwind(className, methodName, exception);

        // Check if the active frame is set correctly for the thread
        assertEquals(frame, Profile._threadActiveFrame.get(1L));
    }

    @Test
    public void testAlloc() {
        String className = "TestClass";

        // Add some dummy data
        Profile._threadActiveFrame = new HashMap<Long, Frame>();
        Profile._threadActiveFrame.put(1L, new Frame(null, new Method("Test", "Method"), 1L));

        Profile.alloc(className);

        // Check if the allocation count is incremented correctly for the class
        ClassAllocation ca = Profile._allocList.get(className);
        assertEquals(1, ca.getAllocCount());
    }
}