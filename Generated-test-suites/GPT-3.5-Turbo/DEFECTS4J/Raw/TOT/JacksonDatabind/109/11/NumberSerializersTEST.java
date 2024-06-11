
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntLikeSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;

public class NumberSerializersTest {
	
	@Mock
	JsonGenerator generatorMock;
	
	@Mock
	SerializerProvider providerMock;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testShortSerializerSerialize() throws IOException {
		ShortSerializer serializer = ShortSerializer.instance;
		
		// Test a typical use case
		Short number = 123;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Short.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testIntegerSerializerSerialize() throws IOException {
		IntegerSerializer serializer = new IntegerSerializer(Integer.class);
		
		// Test a typical use case
		Integer number = 123;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Integer.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testIntLikeSerializerSerialize() throws IOException {
		IntLikeSerializer serializer = IntLikeSerializer.instance;
		
		// Test a typical use case
		Number number = 123;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Integer.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testLongSerializerSerialize() throws IOException {
		LongSerializer serializer = new LongSerializer(Long.class);
		
		// Test a typical use case
		Long number = 123L;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Long.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testFloatSerializerSerialize() throws IOException {
		FloatSerializer serializer = FloatSerializer.instance;
		
		// Test a typical use case
		Float number = 123.45f;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Float.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testDoubleSerializerSerialize() throws IOException {
		DoubleSerializer serializer = new DoubleSerializer(Double.class);
		
		// Test a typical use case
		Double number = 123.45;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an edge case
		number = Double.MAX_VALUE;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
		
		// Test an error scenario
		number = null;
		serializer.serialize(number, generatorMock, providerMock);
		
		// TODO: Assertion
	}
	
	@Test
	public void testAddAll() {
		Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
		NumberSerializers.addAll(allDeserializers);
		
		// TODO: Assertion
	}
	
}

