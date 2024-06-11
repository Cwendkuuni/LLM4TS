package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

JavaType mockJavaType() {
JavaType javaType = mock(JavaType.class);
return javaType;
}

@Test
public void testSetReference() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
JavaType ref = mockJavaType();
type.setReference(ref);
assertSame(ref, type.getSelfReferencedType());
}

@Test(expected = IllegalStateException.class)
public void testSetReferenceTwice() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
JavaType ref1 = mockJavaType();
JavaType ref2 = mockJavaType();
type.setReference(ref1);
type.setReference(ref2);
}

@Test
public void testGetSelfReferencedType() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
assertNull(type.getSelfReferencedType());

JavaType ref = mockJavaType();
type.setReference(ref);
assertSame(ref, type.getSelfReferencedType());
}

@Test
public void testGetGenericSignature() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
StringBuilder sb = new StringBuilder();
type.getGenericSignature(sb);
assertNull(sb.toString());

JavaType ref = mockJavaType();
type.setReference(ref);
assertEquals("Ljava/lang/Object;", type.getGenericSignature(sb).toString());
}

@Test
public void testGetErasedSignature() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
StringBuilder sb = new StringBuilder();
type.getErasedSignature(sb);
assertNull(sb.toString());

JavaType ref = mockJavaType();
type.setReference(ref);
assertEquals("Ljava/lang/Object;", type.getErasedSignature(sb).toString());
}

@Test
public void testWithContentType() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
JavaType contentType = mockJavaType();
assertSame(type, type.withContentType(contentType));
}

@Test
public void testWithTypeHandler() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
Object handler = new Object();
assertSame(type, type.withTypeHandler(handler));
}

@Test
public void testWithContentTypeHandler() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
Object handler = new Object();
assertSame(type, type.withContentTypeHandler(handler));
}

@Test
public void testWithValueHandler() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
Object handler = new Object();
assertSame(type, type.withValueHandler(handler));
}

@Test
public void testWithContentValueHandler() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
Object handler = new Object();
assertSame(type, type.withContentValueHandler(handler));
}

@Test
public void testWithStaticTyping() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
assertSame(type, type.withStaticTyping());
}

@Test
public void testToString() {
ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
assertEquals("[recursive type; UNRESOLVED]", type.toString());

JavaType ref = mockJavaType();
type.setReference(ref);
assertEquals("[recursive type; " + ref.getRawClass().getName() + "]", type.toString());
}

@Test
public void testEquals() {
ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
assertFalse(type1.equals(type2));

ResolvedRecursiveType type3 = new ResolvedRecursiveType(Integer.class, null);
assertFalse(type1.equals(type3));

JavaType ref1 = mockJavaType();
ResolvedRecursiveType type4 = new ResolvedRecursiveType(Object.class, null);
type4.setReference(ref1);
assertFalse(type1.equals(type4));

ResolvedRecursiveType type5 = new ResolvedRecursiveType(Object.class, null);
JavaType ref2 = mockJavaType();
type5.setReference(ref2);
assertTrue(type1.equals(type5));
}
}