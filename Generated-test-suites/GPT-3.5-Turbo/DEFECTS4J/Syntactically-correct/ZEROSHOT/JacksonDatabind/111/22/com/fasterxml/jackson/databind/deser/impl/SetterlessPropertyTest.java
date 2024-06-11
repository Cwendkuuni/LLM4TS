import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

	private SetterlessProperty setterlessProperty;

	@Before
	public void setup() {
		AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
		Method getterMethod = mock(Method.class);

		BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
		JavaType type = mock(JavaType.class);
		TypeDeserializer typeDeser = mock(TypeDeserializer.class);
		Annotations contextAnnotations = mock(Annotations.class);

		when(annotatedMethod.getAnnotated()).thenReturn(getterMethod);

		setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, annotatedMethod);
	}

	@Test
	public void testWithName() {
		PropertyName newName = mock(PropertyName.class);
		SettableBeanProperty result = setterlessProperty.withName(newName);

		assertEquals(newName, result.getFullName());
	}

	@Test
	public void testWithValueDeserializer() {
		JsonDeserializer<?> deser = mock(JsonDeserializer.class);
		SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);

		assertEquals(deser, result.getValueDeserializer());
	}

	@Test
	public void testWithNullProvider() {
		NullValueProvider nva = mock(NullValueProvider.class);
		SettableBeanProperty result = setterlessProperty.withNullProvider(nva);

		assertEquals(nva, result.getNullProvider());
	}

	@Test
	public void testFixAccess() {
		DeserializationConfig config = mock(DeserializationConfig.class);

		setterlessProperty.fixAccess(config);

		verify(config).isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS);
		verifyNoMoreInteractions(config);
	}

	@Test
	public void testGetAnnotation() {
		Class<A> acls = mock(Class.class);
		Annotation expected = mock(Annotation.class);

		AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
		when(annotatedMember.getAnnotation(acls)).thenReturn(expected);
		setterlessProperty = new SetterlessProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class),
				mock(TypeDeserializer.class), mock(Annotations.class), annotatedMember);

		Annotation result = setterlessProperty.getAnnotation(acls);

		assertEquals(expected, result);
	}

	@Test
	public void testGetMember() {
		AnnotatedMember expected = mock(AnnotatedMember.class);
		AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
		when(annotatedMember.getMember()).thenReturn(expected);

		setterlessProperty = new SetterlessProperty(mock(BeanPropertyDefinition.class), mock(JavaType.class),
				mock(TypeDeserializer.class), mock(Annotations.class), annotatedMember);

		AnnotatedMember result = setterlessProperty.getMember();

		assertEquals(expected, result);
	}

	@Test
	public void testDeserializeAndSet_NullToken() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verifyZeroInteractions(p);
	}

	@Test
	public void testDeserializeAndSet_TypeDeserializerNotNull() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
		JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
		when(setterlessProperty.getType()).thenReturn(mock(JavaType.class));
		when(setterlessProperty.getTypeDeserializer()).thenReturn(typeDeserializer);
		when(valueDeserializer.deserializeWithType(p, ctxt, typeDeserializer)).thenReturn(instance);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verify(valueDeserializer).deserializeWithType(p, ctxt, typeDeserializer);
	}

	@Test
	public void testDeserializeAndSet_NullToModify() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
		when(setterlessProperty.getType()).thenReturn(mock(JavaType.class));
		when(setterlessProperty.getTypeDeserializer()).thenReturn(null);
		when(setterlessProperty.getName()).thenReturn(mock(PropertyName.class));
		when(setterlessProperty.getGetter().invoke(instance, (Object[]) null)).thenReturn(null);
		setterlessProperty.setValueDeserializer(valueDeserializer);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verify(ctxt).reportBadDefinition(setterlessProperty.getType(), String.format(
				"Problem deserializing 'setterless' property '%s': get method returned null",
				setterlessProperty.getName()));
	}

	@Test
	public void testDeserializeAndSet_Success() throws IOException, SecurityException, NoSuchMethodException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
		Method method = MockSetterlessProperty.class.getDeclaredMethod("getToModify");
		when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
		when(setterlessProperty.getType()).thenReturn(mock(JavaType.class));
		when(setterlessProperty.getTypeDeserializer()).thenReturn(null);
		when(setterlessProperty.getName()).thenReturn(mock(PropertyName.class));
		when(setterlessProperty.getGetter().invoke(instance, (Object[]) null)).thenReturn(method.invoke(instance));
		setterlessProperty.setValueDeserializer(valueDeserializer);

		setterlessProperty.deserializeAndSet(p, ctxt, instance);

		verify(valueDeserializer).deserialize(p, ctxt, method.invoke(instance));
	}

	@Test
	public void testDeserializeSetAndReturn() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);

		Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

		assertEquals(instance, result);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSet() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);

		setterlessProperty.set(instance, value);
	}

	@Test
	public void testSetAndReturn() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);

		Object result = setterlessProperty.setAndReturn(instance, value);

		assertEquals(instance, result);
	}

	private static class MockSetterlessProperty extends SetterlessProperty {
		private static final long serialVersionUID = 1L;

		public MockSetterlessProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser,
				Annotations contextAnnotations, AnnotatedMethod method) {
			super(propDef, type, typeDeser, contextAnnotations, method);
		}

		public Object getToModify() {
			return null;
		}
	}
}

