package com.mentorgen.tools.profile.runtime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.initProfiler();
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertTrue(Profile._threadActiveFrame.isEmpty());
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testInit() {
        Profile.init();
        assertTrue(Profile._threadActiveFrame.isEmpty());
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.clear();
        assertTrue(Profile._threadActiveFrame.isEmpty());
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }

    @Test
    public void testStart() {
        Profile.start();
        assertTrue(Profile._controller.getProfileStatus());
    }

    @Test
    public void testStop() {
        Profile.stop();
        assertFalse(Profile._controller.getProfileStatus());
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();
        assertFalse(Controller._profile);
        assertTrue(Profile.frameList().isEmpty());
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        assertNotNull(threads);
    }

    @Test
    public void testInteractions() {
        long threadId = Thread.currentThread().getId();
        Iterable<Frame> interactions = Profile.interactions(threadId);
        assertNotNull(interactions);
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertNotNull(allocations);
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadId = Thread.currentThread().getId();
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.sortFrameList(null);
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
    }

    @Test
    public void testStartWithClassNameAndMethodName() {
        String className = "com.test.TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Frame frame = Profile.frameList().iterator().next();
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }

    @Test
    public void testEndWithClassNameAndMethodName() {
        String className = "com.test.TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.end(className, methodName);
        Frame frame = Profile.frameList().iterator().next();
        assertTrue(frame.getEndTime() > 0);
    }

    @Test
    public void testBeginWaitWithClassNameAndMethodName() {
        String className = "com.test.TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Frame frame = Profile.frameList().iterator().next();
        assertTrue(frame.getWaitTime() > 0);
    }

    @Test
    public void testEndWaitWithClassNameAndMethodName() {
        String className = "com.test.TestClass";
        String methodName = "testMethod";
        Profile.start(className, methodName);
        Profile.beginWait(className, methodName);
        Profile.endWait(className, methodName);
        Frame frame = Profile.frameList().iterator().next();
        assertTrue(frame.getWaitTime() > 0);
    }

    @Test
    public void testUnwindWithClassNameAndMethodNameAndException() {
        String className = "com.test.TestClass";
        String methodName = "testMethod";
        String exception = "Exception";
        Profile.unwind(className, methodName, exception);
        Frame frame = Profile.frameList().iterator().next();
        assertEquals(className, frame.getClassName());
        assertEquals(methodName, frame.getMethodName());
    }

    @Test
    public void testAllocWithClassName() {
        String className = "com.test.TestClass";
        Profile.alloc(className);
        ClassAllocation allocation = Profile.allocations().iterator().next();
        assertEquals(className, allocation.getClassName());
    }

    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        assertFalse(Controller._profile);
        assertTrue(Profile.frameList().isEmpty());
    }
}