package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

public class AnyGetterWriterTest {

    @Mock
    private BeanProperty beanPropertyMock;
    
    @Mock
    private AnnotatedMember annotatedMemberMock;
    
    @Mock
    private MapSerializer mapSerializerMock;

    private AnyGetterWriter anyGetterWriter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        anyGetterWriter = new AnyGetterWriter(beanPropertyMock, annotatedMemberMock, mapSerializerMock);
    }

    @Test
    public void testGetAndSerialize_withNullBean() throws Exception {
        try {
            anyGetterWriter.getAndSerialize(null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetAndSerialize_withNonMapValue() throws Exception {
        try {
            anyGetterWriter.getAndSerialize(new Object(), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMemberMock.getName() + "()) not java.util.Map but java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testGetAndFilter_withNullBean() throws Exception {
        try {
            anyGetterWriter.getAndFilter(null, null, null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testGetAndFilter_withNonMapValue() throws Exception {
        try {
            anyGetterWriter.getAndFilter(new Object(), null, null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Value returned by 'any-getter' (" + annotatedMemberMock.getName() + "()) not java.util.Map but java.lang.Object", e.getMessage());
        }
    }
    
    @Test
    public void testResolve() throws Exception {
        anyGetterWriter.resolve(null);
        // TODO: Verify if the handlePrimaryContextualization method is invoked or not
    }
}