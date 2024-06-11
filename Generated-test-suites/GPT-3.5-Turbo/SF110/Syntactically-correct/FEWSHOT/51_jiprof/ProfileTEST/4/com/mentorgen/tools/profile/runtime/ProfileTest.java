
package com.mentorgen.tools.profile.runtime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.init();
    }

    @After
    public void tearDown() {
        Profile.clear();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();

        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    @Test
    public void testInit() {
        Profile.init();

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

        assertEquals(0, Profile._threadActiveFrame.size());
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }

    @Test
    public void testStart() {
        Profile.start();

        verify(Profile._controller).start();
    }

    @Test
    public void testStop() {
        Profile.stop();

        verify(Profile._controller).stop();
    }

    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);

        verify(Profile._controller).setFileName(eq(fileName));
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();

        assertFalse(Controller._profile);
        verify(Profile._threadDictionary, times(1)).threads();
        verify(Profile._threadDictionary, times(1)).getMostRecentFrame(anyLong());
    }

    @Test
    public void testThreads() {
        assertNotNull(Profile.threads());
    }

    @Test
    public void testInteractions() {
        long threadId = Thread.currentThread().getId();
        assertNotNull(Profile.interactions(threadId));
    }

    @Test
    public void testFrameList() {
        assertNotNull(Profile.frameList());
    }

    @Test
    public void testAllocations() {
        assertNotNull(Profile.allocations());
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadId = Thread.currentThread().getId();
        assertNotNull(Profile.getThreadTotalTime(threadId));
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = mock(Comparator.class);

        Profile.sortFrameList(comp);

        verify(Profile._frameList).sort(eq(comp));
    }

    @Test
    public void testStartMethod() {
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.start(className, methodName);

        verify(Profile._frameList, times(1)).add(any(Frame.class));
        verify(Profile._methodDictionary, times(1)).put(anyString(), any(Method.class));
        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), any(Frame.class));
    }

    @Test
    public void testEndMethod() {
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.end(className, methodName);

        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), isNull());
    }

    @Test
    public void testBeginWait() {
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.beginWait(className, methodName);

        verify(Profile._frameList, times(1)).add(any(Frame.class));
    }

    @Test
    public void testEndWait() {
        String className = "TestClass";
        String methodName = "testMethod";

        Profile.endWait(className, methodName);

        verify(Profile._frameList, times(1)).add(any(Frame.class));
    }

    @Test
    public void testUnwind() {
        String className = "TestClass";
        String methodName = "testMethod";
        String exception = "TestException";

        Profile.unwind(className, methodName, exception);

        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), any(Frame.class));
    }

    @Test
    public void testAlloc() {
        String className = "TestClass";

        Profile.alloc(className);

        verify(Profile._allocList, times(1)).get(eq(className));
        verify(Profile._allocList, times(1)).put(anyString(), any(ClassAllocation.class));
    }

    @Test
    public void testRun() throws IOException {
        Profile profile = new Profile();
        Profile._threadDictionary = mock(ThreadDictionary.class);

        profile.run();

        verify(profile._controller).close();
        verify(profile._controllerThread).interrupt();
    }
}
