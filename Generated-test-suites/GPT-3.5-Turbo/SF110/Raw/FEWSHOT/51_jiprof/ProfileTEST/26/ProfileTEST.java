
package com.mentorgen.tools.profile.runtime;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Comparator;

import static org.junit.Assert.*;

public class ProfileTest {

    @Before
    public void setup() {
        Profile.init();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();

        // Assertions
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testInit() {
        Profile.init();

        // Assertions
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

        // Assertions
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
        assertTrue(Profile._threadActiveFrame.isEmpty());
        assertTrue(Profile._threadDictionary.isEmpty());
        assertTrue(Profile._methodDictionary.isEmpty());
        assertTrue(Profile._frameList.isEmpty());
        assertTrue(Profile._allocList.isEmpty());
    }

    @Test
    public void testStart() {
        Profile.start();

        // Assertions
        assertTrue(Profile._controller.isRunning());
    }

    @Test
    public void testStop() {
        Profile.stop();

        // Assertions
        assertFalse(Profile._controller.isRunning());
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);

        // Assertions
        assertEquals(fileName, Profile._controller.getFileName());
    }

    @Test
    public void testShutdown() {
        // Stubbing
        Profile._threadDictionary.add(1l, new Frame(null, null, 1l));
        Profile._threadDictionary.add(2l, new Frame(null, null, 2l));
        Profile._frameList.add(new Frame(null, null, 1l));
        Profile._frameList.add(new Frame(null, null, 2l));

        Profile.shutdown();

        // Assertions
         assertFalse(Controller._profile);
        for (Long threadId : Profile._threadDictionary.threads()) {
            Frame f = Profile._threadDictionary.getMostRecentFrame(threadId);
            assertNull(f.getParent());
            assertTrue(f.isClosed());
        }
        for (Frame frame : Profile.frameList()) {
            assertTrue(frame.getNetTime() >= 0);
        }
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();

        // Assertions
        assertNotNull(threads);
        assertFalse(threads.iterator().hasNext());
    }

    @Test
    public void testInteractions() {
        long threadId = 1l;
        Iterable<Frame> interactions = Profile.interactions(threadId);

        // Assertions
        assertNotNull(interactions);
        assertFalse(interactions.iterator().hasNext());
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();

        // Assertions
        assertNotNull(frameList);
        assertFalse(frameList.iterator().hasNext());
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();

        // Assertions
        assertNotNull(allocations);
        assertFalse(allocations.iterator().hasNext());
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadId = 1l;
        long totalTime = Profile.getThreadTotalTime(threadId);

        // Assertions
        assertEquals(0, totalTime);
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            @Override
            public int compare(Frame f1, Frame f2) {
                // Custom compare logic for testing
                return f1.hashCode() - f2.hashCode();
            }
        };

        Profile.sortFrameList(comp);

        // Assertions
        assertTrue(Profile._frameList.isEmpty());
    }

    @Test
    public void testStartMethod() {
        String className = "Test";
        String methodName = "startMethod";

        Profile.start(className, methodName);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testEndMethod() {
        String className = "Test";
        String methodName = "endMethod";

        Profile.end(className, methodName);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testBeginWait() {
        String className = "Test";
        String methodName = "beginWait";

        Profile.beginWait(className, methodName);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testEndWait() {
        String className = "Test";
        String methodName = "endWait";

        Profile.endWait(className, methodName);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testUnwind() {
        String className = "Test";
        String methodName = "unwind";
        String exception = "TestException";

        Profile.unwind(className, methodName, exception);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testAlloc() {
        String className = "Test";

        Profile.alloc(className);

        // Assertions
        // Add your assertions here
    }

    @Test
    public void testRun() {
        Profile profile = new Profile();

        // Assertions
        // Add your assertions here
    }
}
