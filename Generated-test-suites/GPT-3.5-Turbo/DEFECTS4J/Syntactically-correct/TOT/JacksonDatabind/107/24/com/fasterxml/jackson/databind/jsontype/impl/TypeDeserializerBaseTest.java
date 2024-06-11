import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.ClassUtil;



public class TypeDeserializerBaseTest {
	
	private TypeDeserializerBase typeDeserializerBase;
	private JavaType baseType;
	private TypeIdResolver idResolver;
	private String typePropertyName;
	private boolean typeIdVisible;
	private JavaType defaultImpl;
	private BeanProperty property;
	
	@Before
	public void setUp() {
		baseType = mock(JavaType.class);
		idResolver = mock(TypeIdResolver.class);
		typePropertyName = "type";
		typeIdVisible = true;
		defaultImpl = mock(JavaType.class);
		property = mock(BeanProperty.class);
		typeDeserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

	}

	@Test
	public void testForProperty() {
		TypeDeserializerBase result = typeDeserializerBase.forProperty(property);
		assertNotNull(result);
		assertEquals(baseType, result._baseType);
		assertEquals(idResolver, result._idResolver);
		assertEquals(typePropertyName, result._typePropertyName);
		assertEquals(typeIdVisible,result._typeIdVisible);
		assertEquals(defaultImpl,result._defaultImpl);
		assertEquals(property,result._property);
		
	}

	@Test
	public void testGetTypeInclusion() {
		TypeInfo tc4 = typeDeserializerBase.getTypeInclusion();
		assertEquals(JsonTypeInfo.As.PROPERTY, tc4);
	}

	@Test
	public void testBaseTypeName() {
		when(baseType.getRawClass()).thenReturn(String.class);
		assertEquals(String.class.getName(), typeDeserializerBase.baseTypeName());
	}

	@Test
	public void testGetPropertyName() {
		assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
	}

	@Test
	public void testGetTypeIdResolver() {
		assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
	}

	@Test
	public void testGetDefaultImpl() {
		Class expectedResult = String.class;
		when(defaultImpl.getRawClass()).thenReturn(expectedResult);
		assertEquals(expectedResult, typeDeserializerBase.getDefaultImpl());
	}

	@Test
	public void testBaseType() {
		assertEquals(baseType, typeDeserializerBase.baseType());
	}

	@Test
	public void testToString() {
		String expectedResult = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:"
				+ baseType
				+ "; id-resolver: "
				+ idResolver
				+ "]";

		assertEquals(expectedResult, typeDeserializerBase.toString());
	}

	@Test
	public void testDeserializeWithNativeTypeId() throws IOException {
		JsonParser jp = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object typeId = "Test123";
		Object expectedResult = new Object();
		JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
		
		when(jp.getTypeId()).thenReturn(null);
		when(jp.getTypeId()).thenReturn(typeId);
		when(deser.deserialize(jp, ctxt)).thenReturn(expectedResult);
		
		Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
		assertNull(result);
		
		result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testHandleUnknownTypeId() throws IOException {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		String typeId = "Test123";
		String extraDesc = "type1,type2,type3";
		JavaType expectedResult = mock(JavaType.class);
		when(idResolver.getDescForKnownTypeIds()).thenReturn(extraDesc);
		when(ctxt.handleUnknownTypeId(baseType, typeId, idResolver, extraDesc)).thenReturn(expectedResult);
		JavaType result = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
		assertEquals(expectedResult, result);

	}

	@Test
	public void testHandleMissingTypeId() throws IOException {
		DeserializationContext ctxt = mock(DeserializationContext.class);
		String extraDesc = "type1,type2,type3";
		JavaType expectedResult = mock(JavaType.class);
		when(ctxt.handleMissingTypeId(baseType, idResolver, extraDesc)).thenReturn(expectedResult);
		JavaType result = typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
		assertEquals(expectedResult, result);
	}



	private class TypeDeserializerBaseImpl extends TypeDeserializerBase {

		public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
				boolean typeIdVisible, JavaType defaultImpl) {
			super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
		}

		@Override
		public TypeDeserializer forProperty(BeanProperty prop) {
			return new TypeDeserializerBaseImpl(_baseType, _idResolver, _typePropertyName, _typeIdVisible,
					_defaultImpl);
		}

		@Override
		public JsonTypeInfo.As getTypeInclusion() {
			return JsonTypeInfo.As.PROPERTY;
		}

	}
}