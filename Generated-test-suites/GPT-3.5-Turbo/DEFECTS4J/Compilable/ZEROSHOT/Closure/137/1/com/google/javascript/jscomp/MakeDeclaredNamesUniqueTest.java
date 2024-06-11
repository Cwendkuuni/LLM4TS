package com.google.javascript.jscomp;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    private MakeDeclaredNamesUnique makeDeclaredNamesUnique;

    @Before
    public void setUp() {
        makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    }

    @After
    public void tearDown() {
        makeDeclaredNamesUnique = null;
    }

    @Test
    public void testEnterScope() {
        // TODO: Implement test
    }

    @Test
    public void testExitScope() {
        // TODO: Implement test
    }

    @Test
    public void testShouldTraverse() {
        // TODO: Implement test
    }

    @Test
    public void testVisit() {
        // TODO: Implement test
    }

    @Test
    public void testAddDeclaredName() {
        // TODO: Implement test
    }

    @Test
    public void testGetReplacementName() {
        // TODO: Implement test
    }

    @Test
    public void testStripConstIfReplaced() {
        // TODO: Implement test
    }

    @Test
    public void testForChildScope() {
        // TODO: Implement test
    }
}