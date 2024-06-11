import static org.junit.Assert.*;
import org.junit.Test;

public class StdKeySerializersTest {

	@Test
	public void testGetStdKeySerializer() {
		// Typical use case
		JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
		assertNotNull(serializer);
		
		// Edge case: rawKeyType is null
		serializer = StdKeySerializers.getStdKeySerializer(null, null, false);
		assertNotNull(serializer);
		
		// Exception handling: useDefault is false and no match found
		serializer = StdKeySerializers.getStdKeySerializer(null, Double.class, false);
		assertNull(serializer);
	}

	@Test
	public void testGetFallbackKeySerializer() {
		// Typical use case
		JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
		assertNotNull(serializer);
		
		// Edge case: rawKeyType is null
		serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
		assertNotNull(serializer);
		
		// Exception handling: rawKeyType is Enum
		serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
		assertNotNull(serializer);
	}

	@Test
	public void testGetDefault() {
		JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
		assertNotNull(serializer);
	}

	@Test
	public void testDefaultSerialize() throws IOException {
		// Typical use case for Default.TYPE_DATE
		Default defaultSerializer = new Default(Default.TYPE_DATE, java.util.Date.class);
		JsonGenerator g = new JsonFactory().createGenerator(System.out);
		SerializerProvider provider = new DefaultSerializerProvider.Impl();
		defaultSerializer.serialize(new Date(), g, provider);
	}

	@Test
	public void testDynamicSerialize() throws IOException {
		// Typical use case
		Dynamic dynamicSerializer = new Dynamic();
		JsonGenerator g = new JsonFactory().createGenerator(System.out);
		SerializerProvider provider = new DefaultSerializerProvider.Impl();
		dynamicSerializer.serialize("test", g, provider);
	}

	@Test
	public void testStringKeySerialize() throws IOException {
		// Typical use case
		StringKeySerializer stringKeySerializer = new StringKeySerializer();
		JsonGenerator g = new JsonFactory().createGenerator(System.out);
		SerializerProvider provider = new DefaultSerializerProvider.Impl();
		stringKeySerializer.serialize("key", g, provider);
	}

}