
package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

	@Test
	public void testSetReference() {
		// Typical use case - set an initial reference
		JavaType reference = new ResolvedType();
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(reference);
		assertEquals(reference, recursiveType.getSelfReferencedType());
		
		// Error scenario - setting reference multiple times
		try {
			recursiveType.setReference(new ResolvedType());
			fail("Expected IllegalStateException to be thrown");
		} catch (IllegalStateException e) {
			// Expected exception
		}
	}
	
	@Test
	public void testGetGenericSignature() {
		// Typical use case - referenced type is not null
		JavaType referencedType = new ResolvedType();
		referencedType.setGenericSignature(new StringBuilder("Generic Signature"));
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(referencedType);
		StringBuilder expectedSignature = new StringBuilder("Generic Signature");
		StringBuilder returnedSignature = recursiveType.getGenericSignature(new StringBuilder());
		assertEquals(expectedSignature.toString(), returnedSignature.toString());
		
		// Typical use case - referenced type is null
		recursiveType.setReference(null);
		expectedSignature = new StringBuilder();
		returnedSignature = recursiveType.getGenericSignature(new StringBuilder());
		assertEquals(expectedSignature.toString(), returnedSignature.toString());
	}
	
	@Test
	public void testGetErasedSignature() {
		// Typical use case - referenced type is not null
		JavaType referencedType = new ResolvedType();
		referencedType.setErasedSignature(new StringBuilder("Erased Signature"));
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(referencedType);
		StringBuilder expectedSignature = new StringBuilder("Erased Signature");
		StringBuilder returnedSignature = recursiveType.getErasedSignature(new StringBuilder());
		assertEquals(expectedSignature.toString(), returnedSignature.toString());
		
		// Typical use case - referenced type is null
		recursiveType.setReference(null);
		expectedSignature = new StringBuilder();
		returnedSignature = recursiveType.getErasedSignature(new StringBuilder());
		assertEquals(expectedSignature.toString(), returnedSignature.toString());
	}
	
	@Test
	public void testWithContentType() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		JavaType contentType = new ResolvedType();
		assertEquals(recursiveType, recursiveType.withContentType(contentType));
	}
	
	@Test
	public void testWithTypeHandler() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		Object handler = new Object();
		assertEquals(recursiveType, recursiveType.withTypeHandler(handler));
	}
	
	@Test
	public void testWithContentTypeHandler() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		Object handler = new Object();
		assertEquals(recursiveType, recursiveType.withContentTypeHandler(handler));
	}
	
	@Test
	public void testWithValueHandler() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		Object handler = new Object();
		assertEquals(recursiveType, recursiveType.withValueHandler(handler));
	}
	
	@Test
	public void testWithContentValueHandler() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		Object handler = new Object();
		assertEquals(recursiveType, recursiveType.withContentValueHandler(handler));
	}
	
	@Test
	public void testWithStaticTyping() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		assertEquals(recursiveType, recursiveType.withStaticTyping());
	}
	
	@Test
	public void testNarrow() {
		// Typical use case - return the same instance
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(new ResolvedType());
		assertEquals(recursiveType, recursiveType._narrow(ResolvedType.class));
	}
	
	@Test
	public void testRefine() {
		// Typical use case - return null
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		assertEquals(null, recursiveType.refine(ResolvedType.class, null, null, null));
	}
	
	@Test
	public void testIsContainerType() {
		// Typical use case - return false
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		assertEquals(false, recursiveType.isContainerType());
	}
	
	@Test
	public void testToString() {
		// Typical use case - referenced type is not null
		JavaType referencedType = new ResolvedType();
		referencedType.setRawClass(ResolvedType.class);
		ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType.setReference(referencedType);
		String expectedString = "[recursive type; com.fasterxml.jackson.databind.type.ResolvedType]";
		String returnedString = recursiveType.toString();
		assertEquals(expectedString, returnedString);
		
		// Typical use case - referenced type is null
		recursiveType.setReference(null);
		expectedString = "[recursive type; UNRESOLVED]";
		returnedString = recursiveType.toString();
		assertEquals(expectedString, returnedString);
	}
	
	@Test
	public void testEquals() {
		// Typical use case - equal objects
		JavaType referencedType1 = new ResolvedType();
		referencedType1.setRawClass(ResolvedType.class);
		ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType1.setReference(referencedType1);
		
		JavaType referencedType2 = new ResolvedType();
		referencedType2.setRawClass(ResolvedType.class);
		ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType2.setReference(referencedType2);
		
		assertEquals(true, recursiveType1.equals(recursiveType2));
		
		// Typical use case - different objects
		JavaType referencedType3 = new ResolvedType();
		referencedType3.setRawClass(ResolvedType.class);
		ResolvedRecursiveType recursiveType3 = new ResolvedRecursiveType(ResolvedType.class, null);
		recursiveType3.setReference(referencedType3);
		
		JavaType referencedType4 = new ResolvedType();
		referencedType4.setRawClass(Object.class);
		ResolvedRecursiveType recursiveType4 = new ResolvedRecursiveType(Object.class, null);
		recursiveType4.setReference(referencedType4);
		
		assertEquals(false, recursiveType3.equals(recursiveType4));
	}
}
