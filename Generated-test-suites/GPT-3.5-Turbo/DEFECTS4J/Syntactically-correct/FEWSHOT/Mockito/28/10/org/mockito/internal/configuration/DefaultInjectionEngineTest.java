package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultInjectionEngineTest {
    private DefaultInjectionEngine injectionEngine;
    private Field field1;
    private Field field2;
    private Field field3;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();

        field1 = new Field();
        field1.setType(MockObject.class);
        field1.setAnnotations(new Annotation[] {mock(InjectMocks.class)});
        
        field2 = new Field();
        field2.setType(MockObject.class);
        field2.setAnnotations(new Annotation[] {mock(InjectMocks.class)});
        
        field3 = new Field();
        field3.setType(MockObject.class);
        field3.setAnnotations(new Annotation[] {mock(InjectMocks.class)});
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<Field>();
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);
        injectMocksFields.add(field3);

        Set<Object> mocks = new HashSet<Object>();
        mocks.add(mock(Object1.class));
        mocks.add(mock(Object2.class));
        mocks.add(mock(Object3.class));

        Object testClassInstance = new TestClass();

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mock(Object1.class)).setProperty1();
        verify(mock(Object2.class)).setProperty2();
        verify(mock(Object3.class)).setProperty3();
    }
    
    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = TestClass.class;
        
        Field[] expectedFields = {field1, field2, field3};
        Field[] actualFields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
        
        assertArrayEquals(expectedFields, actualFields);
    }
}