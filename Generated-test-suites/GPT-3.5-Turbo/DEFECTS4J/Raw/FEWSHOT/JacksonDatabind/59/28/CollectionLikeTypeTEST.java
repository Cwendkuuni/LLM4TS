package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.reflect.TypeVariable;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class CollectionLikeTypeTest {
	
	private CollectionLikeType collectionLikeType;
	
	private Class<?> collT;
	private TypeBindings bindings;
    private JavaType superClass;
    private JavaType[] superInts;
    private JavaType elemT;
    private Object valueHandler;
    private Object typeHandler;
    private boolean asStatic;
	
	@Before
	public void setUp() {
		collT = Collection.class;
		bindings = TypeBindings.emptyBindings();
		superClass = SimpleType.construct(Object.class);
		superInts = new JavaType[] { SimpleType.construct(CharSequence.class) };
		elemT = SimpleType.construct(String.class);
		valueHandler = null;
		typeHandler = null;
		asStatic = false;
		
		collectionLikeType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
	}
	
	@Test
	public void testConstruct() {
		CollectionLikeType expected = new CollectionLikeType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
		CollectionLikeType actual = CollectionLikeType.construct(collT, bindings, superClass, superInts, elemT);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testUpgradeFrom() {
		JavaType baseType = SimpleType.construct(Object.class);
		JavaType elementType = SimpleType.construct(Integer.class);
		
		CollectionLikeType expected = new CollectionLikeType((TypeBase)baseType, elementType);
		CollectionLikeType actual = CollectionLikeType.upgradeFrom(baseType, elementType);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_narrow() {
		Class<?> subclass = Collection.class;
		
		CollectionLikeType expected = new CollectionLikeType(subclass, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
		JavaType actual = collectionLikeType._narrow(subclass);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWithContentType() {
		JavaType contentType = SimpleType.construct(Integer.class);
		
		CollectionLikeType expected = new CollectionLikeType(collT, bindings, superClass, superInts, contentType, valueHandler, typeHandler, asStatic);
		JavaType actual = collectionLikeType.withContentType(contentType);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testWithStaticTyping() {
		CollectionLikeType expected = new CollectionLikeType(collT, bindings, superClass, superInts, elemT.withStaticTyping(), valueHandler, typeHandler, true);
		JavaType actual = collectionLikeType.withStaticTyping();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testEquals() {
		CollectionLikeType sameType = new CollectionLikeType(collT, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
		CollectionLikeType differentType = new CollectionLikeType(Integer.class, bindings, superClass, superInts, elemT, valueHandler, typeHandler, asStatic);
		
		assertEquals(collectionLikeType, sameType);
		assertFalse(collectionLikeType.equals(differentType));
	}
	
	@Test
	public void testToString() {
		String expected = "[collection-like type; class "+collT.getName()+", contains "+elemT+"]";
		String actual = collectionLikeType.toString();
		
		assertEquals(expected, actual);
	}

}