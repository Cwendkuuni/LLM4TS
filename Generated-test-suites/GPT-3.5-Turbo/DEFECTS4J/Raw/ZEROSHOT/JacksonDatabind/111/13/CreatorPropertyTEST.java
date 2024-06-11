package com.fasterxml.jackson.databind.deser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() throws Exception {
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    }

    @After
    public void tearDown() throws Exception {
        creatorProperty = null;
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testInject() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testGetAnnotation() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testGetMember() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testGetCreatorIndex() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testSet() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testSetAndReturn() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testGetInjectableValueId() throws Exception {
        // TODO: Implement test
    }

    @Test
    public void testToString() throws Exception {
        // TODO: Implement test
    }
}