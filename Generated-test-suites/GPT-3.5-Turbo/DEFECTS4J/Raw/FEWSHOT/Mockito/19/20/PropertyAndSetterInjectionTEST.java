package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PropertyAndSetterInjectionTest {

  private PropertyAndSetterInjection propertyAndSetterInjection;
  
  @Before
  public void setUp() {
    propertyAndSetterInjection = new PropertyAndSetterInjection();
  }
  
  @Test
  public void testProcessInjection_noInjection() {
    Field injectMocksField = null;
    Object injectMocksFieldOwner = null;
    Set<Object> mockCandidates = new HashSet<Object>();
    
    boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    
    assertFalse(result);
  }
  
  @Test
  public void testProcessInjection_injectionOccurred() {
    Field injectMocksField = null;
    Object injectMocksFieldOwner = null;
    Set<Object> mockCandidates = new HashSet<Object>();
    
    mockCandidates.add(mock(Object.class));
    mockCandidates.add(mock(Object.class));
    mockCandidates.add(mock(Object.class));
    
    boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
    
    assertTrue(result);
  }
  
  @Test
  public void testInjectMockCandidates_noInjection() {
    Class<?> awaitingInjectionClazz = Object.class;
    Set<Object> mocks = new HashSet<Object>();
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    Object instance = new Object();
    
    boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    
    assertFalse(result);
  }
  
  @Test
  public void testInjectMockCandidates_injectionOccurred() {
    Class<?> awaitingInjectionClazz = Object.class;
    Set<Object> mocks = new HashSet<Object>();
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    Object instance = new Object();
    
    boolean result = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);
    
    assertTrue(result);
  }
  
  @Test
  public void testInjectMockCandidatesOnFields_noInjection() {
    Set<Object> mocks = new HashSet<Object>();
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    Object instance = new Object();
    boolean injectionOccurred = false;
    List<Field> orderedInstanceFields = new ArrayList<>();
    orderedInstanceFields.add(mock(Field.class));
    orderedInstanceFields.add(mock(Field.class));
    orderedInstanceFields.add(mock(Field.class));
    
    boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
    
    assertFalse(result);
  }
  
  @Test
  public void testInjectMockCandidatesOnFields_injectionOccurred() {
    Set<Object> mocks = new HashSet<Object>();
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    mocks.add(mock(Object.class));
    Object instance = new Object();
    boolean injectionOccurred = false;
    List<Field> orderedInstanceFields = new ArrayList<>();
    orderedInstanceFields.add(mock(Field.class));
    orderedInstanceFields.add(mock(Field.class));
    orderedInstanceFields.add(mock(Field.class));
    
    boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, injectionOccurred, orderedInstanceFields);
    
    assertTrue(result);
  }
  
  @Test
  public void testInitializeInjectMocksField() {
    Field field = mock(Field.class);
    Object fieldOwner = new Object();
    
    FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
    
    assertNotNull(report);
  }
  
  @Test(expected = Reporter.class)
  public void testInitializeInjectMocksField_fieldInitialisationThrewException() {
    Field field = mock(Field.class);
    Object fieldOwner = new Object();
    when(field.getName()).thenReturn("field");
    doThrow(new MockitoException()).when(fieldOwner).getField();
    
    propertyAndSetterInjection.initializeInjectMocksField(field, fieldOwner);
    
    verify(field, times(1)).getName();
  }
  
  @Test
  public void testOrderedInstanceFieldsFrom() {
    Class<?> awaitingInjectionClazz = Object.class;
    
    List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    
    assertNotNull(orderedInstanceFields);
  }
}