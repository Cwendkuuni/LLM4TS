import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
	
	private SimpleType simpleType;

	@Before
	public void setUp() throws Exception {
		simpleType = SimpleType.constructUnsafe(String.class);
	}

	@After
	public void tearDown() throws Exception {
		simpleType = null;
	}

	@Test
	public void testConstructUnsafe() {
		assertEquals(String.class, simpleType.getRawClass());
		assertTrue(simpleType.getBindings().isEmpty());
		assertNull(simpleType.getSuperClass());
		assertNull(simpleType.getSuperInterfaces());
		assertNull(simpleType.getValueHandler());
		assertNull(simpleType.getTypeHandler());
		assertFalse(simpleType.isStatic());
	}

	@Test
	public void testNarrow() {
		JavaType subclass = SimpleType.constructUnsafe(Object.class);
		JavaType narrowedType = simpleType.narrow(subclass.getRawClass());
		assertEquals(subclass, narrowedType);
	}

	@Test
	public void testWithContentType() {
		assertThrows(IllegalArgumentException.class, () -> {
			simpleType.withContentType(simpleType);
		});
	}

	@Test
	public void testWithTypeHandler() {
		Object typeHandler = new Object();
		SimpleType newType = simpleType.withTypeHandler(typeHandler);
		assertEquals(typeHandler, newType.getTypeHandler());
	}

	@Test
	public void testWithContentTypeHandler() {
		assertThrows(IllegalArgumentException.class, () -> {
			simpleType.withContentTypeHandler(simpleType);
		});
	}

	@Test
	public void testWithStaticTyping() {
		SimpleType newType = simpleType.withStaticTyping();
		assertTrue(newType.isStatic());
	}

	@Test
	public void testRefine() {
		JavaType rawType = SimpleType.constructUnsafe(List.class);
		TypeBindings bindings = new TypeBindings();
		JavaType superClass = SimpleType.constructUnsafe(Collection.class);
		JavaType[] superInterfaces = null;
		JavaType refinedType = simpleType.refine(rawType.getRawClass(), bindings, superClass, superInterfaces);
		assertNull(refinedType);
	}

	@Test
	public void testBuildCanonicalName() {
		String canonicalName = simpleType.buildCanonicalName();
		assertEquals("java.lang.String", canonicalName);
	}

	@Test
	public void testIsContainerType() {
		assertFalse(simpleType.isContainerType());
	}

	@Test
	public void testGetErasedSignature() {
		StringBuilder sb = simpleType.getErasedSignature(new StringBuilder());
		assertEquals("Ljava/lang/String;", sb.toString());
	}

	@Test
	public void testGetGenericSignature() {
		StringBuilder sb = simpleType.getGenericSignature(new StringBuilder());
		assertEquals("Ljava/lang/String;", sb.toString());
	}

	@Test
	public void testToString() {
		String expected = "[simple type, class java.lang.String]";
		assertEquals(expected, simpleType.toString());
	}

	@Test
	public void testEqualsObject() {
		JavaType equalType = SimpleType.constructUnsafe(String.class);
		assertTrue(simpleType.equals(equalType));
		
		JavaType differentType = SimpleType.constructUnsafe(Integer.class);
		assertFalse(simpleType.equals(differentType));
		
		JavaType typeWithBindings = SimpleType.constructUnsafe(String.class);
		typeWithBindings.getBindings().addBinding("T", Integer.class);
		assertFalse(simpleType.equals(typeWithBindings));
	}

}