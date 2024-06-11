package com.mentorgen.tools.profile.runtime;

import com.mentorgen.tools.profile.*;
import java.util.*;
import com.mentorgen.tools.profile.output.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.initProfiler();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // Assert statements
    }

    @Test
    public void testInit() {
        Profile.init();
        // Assert statements
    }

    @Test
    public void testClear() {
        Profile.clear();
        // Assert statements
    }

    @Test
    public void testStart() {
        Profile.start();
        // Assert statements
    }

    @Test
    public void testStop() {
        Profile.stop();
        // Assert statements
    }

    @Test
    public void testSetFileName() {
        String fileName = "";
        Profile.setFileName(fileName);
        // Assert statements
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();
        // Assert statements
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // Assert statements
    }

    @Test
    public void testInteractions() {
        long threadId = 0;
        Iterable<Frame> interactions = Profile.interactions(threadId);
        // Assert statements
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        // Assert statements
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // Assert statements
    }

    @Test
    public void testGetThreadTotalTime() {
        long threadId = 0;
        long threadTotalTime = Profile.getThreadTotalTime(threadId);
        // Assert statements
    }

    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = null;
        Profile.sortFrameList(comp);
        // Assert statements
    }

    @Test
    public void testStartStringString() {
        String className = "";
        String methodName = "";
        Profile.start(className, methodName);
        // Assert statements
    }

    @Test
    public void testEndStringString() {
        String className = "";
        String methodName = "";
        Profile.end(className, methodName);
        // Assert statements
    }

    @Test
    public void testBeginWait() {
        String className = "";
        String methodName = "";
        Profile.beginWait(className, methodName);
        // Assert statements
    }

    @Test
    public void testEndWait() {
        String className = "";
        String methodName = "";
        Profile.endWait(className, methodName);
        // Assert statements
    }

    @Test
    public void testUnwind() {
        String className = "";
        String methodName = "";
        String exception = "";
        Profile.unwind(className, methodName, exception);
        // Assert statements
    }

    @Test
    public void testAlloc() {
        String className = "";
        Profile.alloc(className);
        // Assert statements
    }

    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        // Assert statements
    }

}