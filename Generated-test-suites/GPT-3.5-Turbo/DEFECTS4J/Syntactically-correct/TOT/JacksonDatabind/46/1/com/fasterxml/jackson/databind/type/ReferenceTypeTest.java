
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

	@Test
	public void testConstruct() {
		ReferenceType reference = ReferenceType.construct(Integer.class, null, null, null);
		assertEquals(Integer.class, reference._class);
	}

	@Test
	public void testWithStaticTyping() {
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertTrue(reference.withStaticTyping()._asStatic);
	}

	@Test
	public void testWithStaticTyping_noChange() {
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, true);
		assertFalse(reference.withStaticTyping()._asStatic);
	}

	@Test
	public void testWithContentValueHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withContentValueHandler(handler)._referencedType.getValueHandler());
	}

	@Test
	public void testWithContentValueHandler_noChange() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(reference._referencedType.getValueHandler(), reference.withContentValueHandler(handler)._referencedType.getValueHandler());
	}

	@Test
	public void testWithContentValueHandler_wrongHandler() {
		Object handler1 = new Object();
		Object handler2 = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertFalse(reference.withContentValueHandler(handler2)._referencedType.getValueHandler() == reference.withContentValueHandler(handler1)._referencedType.getValueHandler());
	}

	@Test
	public void testWithContentValueHandler_sameHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withContentValueHandler(handler)._referencedType.getValueHandler());
	}

	@Test
	public void testWithContentTypeHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withContentTypeHandler(handler)._referencedType.getTypeHandler());
	}

	@Test
	public void testWithContentTypeHandler_noChange() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(reference._referencedType.getTypeHandler(), reference.withContentTypeHandler(handler)._referencedType.getTypeHandler());
	}

	@Test
	public void testWithContentTypeHandler_wrongHandler() {
		Object handler1 = new Object();
		Object handler2 = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertFalse(reference.withContentTypeHandler(handler2)._referencedType.getTypeHandler() == reference.withContentTypeHandler(handler1)._referencedType.getTypeHandler());
	}

	@Test
	public void testWithContentTypeHandler_sameHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withContentTypeHandler(handler)._referencedType.getTypeHandler());
	}

	@Test
	public void testWithValueHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withValueHandler(handler)._valueHandler);
	}

	@Test
	public void testWithValueHandler_noChange() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(reference._valueHandler, reference.withValueHandler(handler)._valueHandler);
	}

	@Test
	public void testWithValueHandler_wrongHandler() {
		Object handler1 = new Object();
		Object handler2 = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertFalse(reference.withValueHandler(handler2)._valueHandler == reference.withValueHandler(handler1)._valueHandler);
	}

	@Test
	public void testWithValueHandler_sameHandler() {
		Object handler = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertEquals(handler, reference.withValueHandler(handler)._valueHandler);
	}

	@Test
	public void testWithContentTypeHandlerValueHandler() {
		Object ch1 = new Object();
		Object ch2 = new Object();
		Object vh1 = new Object();
		Object vh2 = new Object();
		ReferenceType reference = new ReferenceType(Integer.class, null, null, null, false);
		assertFalse(reference.withContentTypeHandler(ch1)._referencedType.getValueHandler() == reference.withContentTypeHandler(ch2)._referencedType.getValueHandler());
		assertFalse(reference.withContentTypeHandler(ch1)._valueHandler == reference.withContentTypeHandler(ch2)._valueHandler);
		assertFalse(reference.withContentValueHandler(vh1)._referencedType.getTypeHandler() == reference.withContentValueHandler(vh2)._referencedType.getTypeHandler());
		assertFalse(reference.withContentValueHandler(vh1)._valueHandler == reference.withContentValueHandler(vh2)._valueHandler);
	}
}
