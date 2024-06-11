package com.mentorgen.tools.profile.runtime;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProfileTest {

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }

    // Additional test cases for other methods will be added by other experts.
}