
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
  
  // Test case for setReference method
  @Test
  public void testSetReference() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of JavaType to use as reference
    JavaType ref = new JavaType(null, null, null, null);
    
    // Call the setReference method
    recursiveType.setReference(ref);
    
    // Assert that the referencedType is set correctly
    assertEquals(ref, recursiveType.getSelfReferencedType());
  }
  
  // Test case for getGenericSignature method
  @Test
  public void testGetGenericSignature() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an empty StringBuilder
    StringBuilder sb = new StringBuilder();
    
    // Call the getGenericSignature method
    StringBuilder result = recursiveType.getGenericSignature(sb);
    
    // Assert that the result is not null
    assertNotNull(result);
    // Assert that the result is the same as the input StringBuilder
    assertEquals(sb, result);
  }
  
  // Test case for getErasedSignature method
  @Test
  public void testGetErasedSignature() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an empty StringBuilder
    StringBuilder sb = new StringBuilder();
    
    // Call the getErasedSignature method
    StringBuilder result = recursiveType.getErasedSignature(sb);
    
    // Assert that the result is not null
    assertNotNull(result);
    // Assert that the result is the same as the input StringBuilder
    assertEquals(sb, result);
  }
  
  // Test case for withContentType method
  @Test
  public void testWithContentType() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of JavaType to use as contentType
    JavaType contentType = new JavaType(null, null, null, null);
    
    // Call the withContentType method
    JavaType result = recursiveType.withContentType(contentType);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for withTypeHandler method
  @Test
  public void testWithTypeHandler() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of an Object to use as typeHandler
    Object typeHandler = new Object();
    
    // Call the withTypeHandler method
    JavaType result = recursiveType.withTypeHandler(typeHandler);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for withContentTypeHandler method
  @Test
  public void testWithContentTypeHandler() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of an Object to use as contentTypeHandler
    Object contentTypeHandler = new Object();
    
    // Call the withContentTypeHandler method
    JavaType result = recursiveType.withContentTypeHandler(contentTypeHandler);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for withValueHandler method
  @Test
  public void testWithValueHandler() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of an Object to use as valueHandler
    Object valueHandler = new Object();
    
    // Call the withValueHandler method
    JavaType result = recursiveType.withValueHandler(valueHandler);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for withContentValueHandler method
  @Test
  public void testWithContentValueHandler() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of an Object to use as contentValueHandler
    Object contentValueHandler = new Object();
    
    // Call the withContentValueHandler method
    JavaType result = recursiveType.withContentValueHandler(contentValueHandler);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for withStaticTyping method
  @Test
  public void testWithStaticTyping() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Call the withStaticTyping method
    JavaType result = recursiveType.withStaticTyping();
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for _narrow method
  @Test
  public void testNarrow() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of a Class to use as subclass
    Class<?> subclass = Object.class;
    
    // Call the _narrow method
    JavaType result = recursiveType._narrow(subclass);
    
    // Assert that the result is the same as the input RecursiveRecursiveType
    assertEquals(recursiveType, result);
  }
  
  // Test case for refine method
  @Test
  public void testRefine() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create instances of rawType, bindings, superClass and superInterfaces
    Class<?> rawType = Object.class;
    TypeBindings bindings = new TypeBindings(null, null);
    JavaType superClass = new JavaType(null, null, null, null);
    JavaType[] superInterfaces = new JavaType[]{};
    
    // Call the refine method
    JavaType result = recursiveType.refine(rawType, bindings, superClass, superInterfaces);
    
    // Assert that the result is null
    assertNull(result);
  }
  
  // Test case for isContainerType method
  @Test
  public void testIsContainerType() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Call the isContainerType method
    boolean result = recursiveType.isContainerType();
    
    // Assert that the result is false
    assertFalse(result);
  }
  
  // Test case for toString method
  @Test
  public void testToString() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Call the toString method
    String result = recursiveType.toString();
    
    // Assert that the result is not null
    assertNotNull(result);
  }
  
  // Test case for equals method when input is null
  @Test
  public void testEqualsNullInput() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Call the equals method with null input
    boolean result = recursiveType.equals(null);
    
    // Assert that the result is false
    assertFalse(result);
  }
  
  // Test case for equals method when input is not an instance of ResolvedRecursiveType
  @Test
  public void testEqualsDifferentClass() {
    // Create an instance of the ResolvedRecursiveType class
    ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(null, null);
    
    // Create an instance of a different class
    Object obj = new Object();
    
    // Call the equals method with the instance of a different class
    boolean result = recursiveType.equals(obj);
    
    // Assert that the result is false
    assertFalse(result);
  }
  
  // Test case for equals method when referencedType is null
  @Test
  public void testEqualsNullReferencedType() {
    // Create the first instance of the ResolvedRecursiveType class with referencedType as null
    ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
    
    // Create the second instance of the ResolvedRecursiveType class with referencedType as null
    ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);
    
    // Call the equals method with the second instance as parameter
    boolean result = recursiveType1.equals(recursiveType2);
    
    // Assert that the result is false
    assertFalse(result);
  }
  
  // Test case for equals method when the referencedType of one instance is different from the referencedType of the other instance
  @Test
  public void testEqualsDifferentReferencedType() {
    // Create an instance of JavaType for referencedType
    JavaType referencedType1 = new JavaType(null, null, null, null);
    JavaType referencedType2 = new JavaType(null, null, null, null);
    
    // Create the first instance of the ResolvedRecursiveType class with referencedType1
    ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
    recursiveType1.setReference(referencedType1);
    
    // Create the second instance of the ResolvedRecursiveType class with referencedType2
    ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);
    recursiveType2.setReference(referencedType2);
    
    // Call the equals method with the second instance as parameter
    boolean result = recursiveType1.equals(recursiveType2);
    
    // Assert that the result is false
    assertFalse(result);
  }
  
  // Test case for equals method when the two instances have the same referencedType
  @Test
  public void testEqualsSameReferencedType() {
    // Create an instance of JavaType for referencedType
    JavaType referencedType = new JavaType(null, null, null, null);
    
    // Create the first instance of the ResolvedRecursiveType class with referencedType
    ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(null, null);
    recursiveType1.setReference(referencedType);
    
    // Create the second instance of the ResolvedRecursiveType class with referencedType
    ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(null, null);
    recursiveType2.setReference(referencedType);
    
    // Call the equals method with the second instance as parameter
    boolean result = recursiveType1.equals(recursiveType2);
    
    // Assert that the result is true
    assertTrue(result);
  }
}

