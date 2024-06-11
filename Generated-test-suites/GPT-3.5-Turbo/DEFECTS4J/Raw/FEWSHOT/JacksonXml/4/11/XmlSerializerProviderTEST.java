
package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

	private XmlSerializerProvider provider;
	private XmlRootNameLookup rootNameLookup;
	private SerializationConfig config;
	private SerializerFactory factory;
	private DefaultSerializerProvider defaultProvider;
	
	
	@Before
	public void setup() {
		rootNameLookup = mock(XmlRootNameLookup.class);
		config = mock(SerializationConfig.class);
		factory = mock(SerializerFactory.class);
		defaultProvider = mock(DefaultSerializerProvider.class);
		provider = new XmlSerializerProvider(rootNameLookup);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(rootNameLookup, provider._rootNameLookup);
	}
	
	@Test
	public void testConstructorWithConfigAndFactory() {
		XmlSerializerProvider newProvider = new XmlSerializerProvider(provider, config, factory);
		assertEquals(provider._rootNameLookup, newProvider._rootNameLookup);
		assertEquals(config, newProvider._config);
		assertEquals(factory, newProvider._serializerFactory);
	}
	
	@Test
	public void testCreateInstance() {
		when(rootNameLookup.findRootName(any(JavaType.class), any(SerializationConfig.class))).thenReturn(new QName("root"));
		provider._initWithRootName(mock(ToXmlGenerator.class), new QName("root"));
		provider.createInstance(config, factory);
		verify(rootNameLookup).findRootName(mock(JavaType.class), config);
	}
	
	@Test
	public void testSerializeValue() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Object value = mock(Object.class);
		provider.serializeValue(generator, value);
		verify(rootNameLookup).findRootName(value.getClass(), provider._config);
	}
	
	@Test
	public void testSerializeValueWithRootType() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Object value = mock(Object.class);
		JavaType rootType = mock(JavaType.class);
		provider.serializeValue(generator, value, rootType);
		verify(rootNameLookup).findRootName(rootType, provider._config);
	}
	
	@Test
	public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
		JsonGenerator generator = mock(JsonGenerator.class);
		Object value = mock(Object.class);
		JavaType rootType = mock(JavaType.class);
		JsonSerializer<Object> ser = mock(JsonSerializer.class);
		provider.serializeValue(generator, value, rootType, ser);
		verify(rootNameLookup).findRootName(rootType, provider._config);
	}
	
	@Test
	public void test_rootNameFromConfig_noConfigName() {
		assertNull(provider._rootNameFromConfig());
	}
	
	@Test
	public void test_rootNameFromConfig_withConfigName() {
		PropertyName name = mock(PropertyName.class);
		when(name.getNamespace()).thenReturn("ns");
		when(name.getSimpleName()).thenReturn("name");
		when(config.getFullRootName()).thenReturn(name);
		assertEquals(new QName("ns", "name"), provider._rootNameFromConfig());
	}
	
	@Test
	public void test_startRootArray() throws IOException {
		ToXmlGenerator xgen = mock(ToXmlGenerator.class);
		QName rootName = new QName("root");
		provider._startRootArray(xgen, rootName);
		verify(xgen).writeStartObject();
		verify(xgen).writeFieldName("item");
	}
	
	@Test
	public void test_initWithRootName_withoutRoot() throws IOException {
		ToXmlGenerator xgen = mock(ToXmlGenerator.class);
		when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
		when(xgen.inRoot()).thenReturn(true);
		QName rootName = new QName("root");
		provider._initWithRootName(xgen, rootName);
		verify(xgen).setNextName(rootName);
	}
	
	@Test
	public void test_initWithRootName_withRoot() throws IOException {
		ToXmlGenerator xgen = mock(ToXmlGenerator.class);
		when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(true);
		QName rootName = new QName("root");
		provider._initWithRootName(xgen, rootName);
		verify(xgen).setNextNameIfMissing(rootName);
	}
	
	@Test
	public void test_asXmlGenerator_notToXmlGenerator() throws JsonMappingException {
		JsonGenerator gen = mock(JsonGenerator.class);
		JsonMappingException exception = null;
		try {
			provider._asXmlGenerator(gen);
		} catch (JsonMappingException e) {
			exception = e;
		}
		assertEquals("XmlMapper does not with generators of type other than ToXmlGenerator; got: org.apache.commons.cli.ParseException", exception.getMessage());
	}
	
	@Test
	public void test_asXmlGenerator_isToXmlGenerator() throws JsonMappingException {
		ToXmlGenerator gen = mock(ToXmlGenerator.class);
		assertEquals(gen, provider._asXmlGenerator(gen));
	}
}
