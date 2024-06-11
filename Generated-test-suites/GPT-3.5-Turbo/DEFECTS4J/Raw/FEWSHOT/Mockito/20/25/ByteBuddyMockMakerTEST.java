package org.mockito.internal.creation.bytebuddy;

import static org.mockito.internal.util.StringJoiner.join;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import org.mockito.exceptions.base.*;
import org.mockito.internal.*;
import org.mockito.internal.configuration.*;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.*;
import org.mockito.mock.*;
import org.mockito.plugins.*;

import org.junit.Test;

public class ByteBuddyMockMakerTest {
  	@Test
    public void testCreateMock() {
      	MockHandler mockHandler = Mockito.mock(MockHandler.class);
      	MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      
      	try {
          	// Test when SerializableMode is ACROSS_CLASSLOADERS
          	Mockito.when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
          	maker.createMock(settings, mockHandler);
          	fail("Should throw MockitoException");
        }
      	catch(MockitoException e) {
        	// Expected exception
        }
      
      	try {
          	// Test when mockedProxyType is generated successfully
          	ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
          	Mockito.when(classInstantiator.instantiate(Mockito.any(Class.class))).thenReturn(new TestClass());
          	CachingMockBytecodeGenerator cachingMockBytecodeGenerator = Mockito.mock(CachingMockBytecodeGenerator.class);
          	Mockito.when(cachingMockBytecodeGenerator.get(Mockito.any(Class.class), Mockito.any(Class[].class))).thenReturn(TestClass.class);
          
          	Field classInstantiatorField = ByteBuddyMockMaker.class.getDeclaredField("classInstantiator");
          	classInstantiatorField.setAccessible(true);
          	classInstantiatorField.set(maker, classInstantiator);
          
          	Field cachingMockBytecodeGeneratorField = ByteBuddyMockMaker.class.getDeclaredField("cachingMockBytecodeGenerator");
          	cachingMockBytecodeGeneratorField.setAccessible(true);
          	cachingMockBytecodeGeneratorField.set(maker, cachingMockBytecodeGenerator);
          
          	maker.createMock(settings, mockHandler);
          
          	// Verify
          	MockMethodInterceptor.MockAccess mockAccess = ((MockMethodInterceptor.MockAccess)maker);
          	Mockito.verify(mockAccess).setMockitoInterceptor(Mockito.any(MockMethodInterceptor.class));
        }
      	catch(Exception e) {
        	fail("Exception");
        }
    }
  
  	@Test
    public void testEnsureMockIsAssignableToMockedType() {
      	MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      	TestClass mock = new TestClass();
      
      	// Test when typeToMock is null
      	Mockito.when(settings.getTypeToMock()).thenReturn(null);
      	assertNull(maker.ensureMockIsAssignableToMockedType(settings, mock));
      
      	// Test when typeToMock is not null
      	Mockito.when(settings.getTypeToMock()).thenReturn(TestClass.class);
      	assertEquals(mock, maker.ensureMockIsAssignableToMockedType(settings, mock));
    }
  
  	@Test
    public void testDescribeClass() {
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      	
      	// Test when type is null
      	Class clazz = null;
      	String expectedResult = "null";
      	assertEquals(expectedResult, maker.describeClass(clazz));
      	
      	// Test when type is not null
      	clazz = TestClass.class;
      	expectedResult = "'TestClass', loaded by classloader : 'TestClassClassLoader'";
		assertEquals(expectedResult, maker.describeClass(clazz));
    }
  
  	@Test
    public void testGetHandler() {
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      	Object mock = new TestClass();
      
      	mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
      	MockHandler mockHandler = Mockito.mock(MockHandler.class);
      
      	Mockito.when(((MockMethodInterceptor.MockAccess)mock).getMockitoInterceptor().getMockHandler()).thenReturn(mockHandler);
      
      	assertEquals(mockHandler, maker.getHandler(mock));
      	
      	mock = new Object();
      	assertEquals(null, maker.getHandler(mock));
    }
  
 		@Test
    public void testResetMock() {
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      	Object mock = new TestClass();
      	MockHandler newHandler = Mockito.mock(MockHandler.class);
      	MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
      
      	// Test when mock is instanceof MockMethodInterceptor.MockAccess
      	mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
      	Mockito.doNothing().when(((MockMethodInterceptor.MockAccess)mock)).setMockitoInterceptor(Mockito.any(MockMethodInterceptor.class));
      	maker.resetMock(mock, newHandler, settings);
      	Mockito.verify(((MockMethodInterceptor.MockAccess)mock)).setMockitoInterceptor(Mockito.any(MockMethodInterceptor.class));
      
      	// Test when mock is not instanceof MockMethodInterceptor.MockAccess
      	mock = new Object();
      	maker.resetMock(mock, newHandler, settings); 
      	// TODO: Add verification
    }
  
  	@Test
    public void testInitializeClassInstantiator() {
      	// Test when Objenesis class is found
      	GlobalConfiguration configuration = Mockito.mock(GlobalConfiguration.class);
      	Mockito.when(configuration.enableClassCache()).thenReturn(true);
      
      	try {
          	Class<?> objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
          	Constructor<?> usingClassCacheConstructor = objenesisClassLoader.getDeclaredConstructor(boolean.class);
          
          	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
          	ClassInstantiator classInstantiator = maker.initializeClassInstantiator();
          	
          	Field enableClassCacheField = GlobalConfiguration.class.getDeclaredField("enableClassCache");
			enableClassCacheField.setAccessible(true);
          	Object enableClassCache = enableClassCacheField.get(configuration);
          	
          	Object result = usingClassCacheConstructor.newInstance(enableClassCache);
          	assertEquals(result, classInstantiator);
        }
      	catch(Exception e) {
        	fail("Exception");
        }
      
      	// Test when Objenesis class is not found
      	ClassInstantiator classInstantiator = null;
      	try {
          	Field classInstantiatorField = ByteBuddyMockMaker.class.getDeclaredField("classInstantiator");
          	classInstantiatorField.setAccessible(true);
          	classInstantiator = (ClassInstantiator)classInstantiatorField.get(new ByteBuddyMockMaker());
          
          	if(classInstantiator != null) {
            	fail("classInstantiator should be null");
            }
        }
      	catch(Exception e) {
        	fail("Exception");
        }
      
      	try {
          	Constructor<?> usingClassCacheConstructor = ByteBuddyMockMaker.class.getDeclaredConstructor();
          	Object result = usingClassCacheConstructor.newInstance();
          	assertEquals(result, classInstantiator);
        }
      	catch(Exception e) {
        	fail("Exception");
        }
    }
  
  	@Test
    public void testAsInternalMockHandler() {
      	ByteBuddyMockMaker maker = new ByteBuddyMockMaker();
      	MockHandler mockHandler = Mockito.mock(MockHandler.class);
      
      	// Test when handler is instanceof InternalMockHandler
      	InternalMockHandler internalMockHandler = Mockito.mock(InternalMockHandler.class);
      	Mockito.when(mockHandler instanceof InternalMockHandler).thenReturn(true);
      	Mockito.when(Mockito.any(InternalMockHandler.class)).thenReturn(internalMockHandler);
      	assertEquals(internalMockHandler, maker.asInternalMockHandler(mockHandler));
      
      	// Test when handler is not instanceof InternalMockHandler
      	Mockito.when(mockHandler instanceof InternalMockHandler).thenReturn(false);
      	try {
          	maker.asInternalMockHandler(mockHandler);
          	fail("Should throw MockitoException");
        }
      	catch(MockitoException e) {
        	// Expected exception
        }
    }
}