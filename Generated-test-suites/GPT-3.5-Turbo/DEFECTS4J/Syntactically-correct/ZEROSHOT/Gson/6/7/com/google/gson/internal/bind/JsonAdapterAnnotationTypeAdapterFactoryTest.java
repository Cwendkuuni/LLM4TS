import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

	private JsonAdapterAnnotationTypeAdapterFactory factory;
	private Gson gson;

	@Before
	public void setUp() {
		ConstructorConstructor constructorConstructor = new ConstructorConstructor();
		factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);
		gson = new Gson();
	}

	@Test
	public void testCreate_WithoutJsonAdapterAnnotation_ReturnsNull() {
		TypeToken<String> targetType = TypeToken.get(String.class);
		TypeAdapter<String> typeAdapter = factory.create(gson, targetType);
		assertNull(typeAdapter);
	}

	@Test
	public void testCreate_WithTypeAdapterAnnotation_ReturnsTypeAdapter() {
		TypeToken<Integer> targetType = TypeToken.get(Integer.class);
		TypeAdapter<Integer> typeAdapter = factory.create(gson, targetType);
		assertNotNull(typeAdapter);
	}

	@Test
	public void testCreate_WithUnsupportedAnnotation_ThrowsIllegalArgumentException() {
		TypeToken<Double> targetType = TypeToken.get(Double.class);
		try {
			factory.create(gson, targetType);
			fail("Expected IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
		}
	}

	@Test
	public void testGetTypeAdapter_WithTypeAdapterAnnotation_ReturnsTypeAdapter() {
		JsonAdapter annotation = MockType.class.getAnnotation(JsonAdapter.class);
		TypeToken<MockType> fieldType = TypeToken.get(MockType.class);
		TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor,
				gson, fieldType, annotation);
		assertNotNull(typeAdapter);
	}

	@Test
	public void testGetTypeAdapter_WithTypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
		JsonAdapter annotation = MockFactory.class.getAnnotation(JsonAdapter.class);
		TypeToken<MockType> fieldType = TypeToken.get(MockType.class);
		TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor,
				gson, fieldType, annotation);
		assertNotNull(typeAdapter);
	}

	@Test
	public void testGetTypeAdapter_WithUnsupportedAnnotation_ThrowsIllegalArgumentException() {
		JsonAdapter annotation = Unsupported.class.getAnnotation(JsonAdapter.class);
		TypeToken<MockType> fieldType = TypeToken.get(MockType.class);
		try {
			JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType,
					annotation);
			fail("Expected IllegalArgumentException was not thrown");
		} catch (IllegalArgumentException e) {
			assertEquals("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.", e.getMessage());
		}
	}

	@Test
	public void testGetTypeAdapter_ReturnsNullSafeTypeAdapter() {
		JsonAdapter annotation = MockType.class.getAnnotation(JsonAdapter.class);
		TypeToken<MockType> fieldType = TypeToken.get(MockType.class);
		TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor,
				gson, fieldType, annotation);
		assertNotNull(typeAdapter);
		assertTrue(typeAdapter.nullSafe());
	}

	@JsonAdapter(MockAdapter.class)
	private static class MockType {

	}

	private static class MockAdapter extends TypeAdapter<MockType> {

		@Override
		public void write(JsonWriter out, MockType value) throws IOException {
			// implementation
		}

		@Override
		public MockType read(JsonReader in) throws IOException {
			return null;
		}
	}

	@JsonAdapter(MockFactory.class)
	private static class MockType {

	}

	private static class MockFactory implements TypeAdapterFactory {

		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
			return null;
		}
	}

	@JsonAdapter(Unsupported.class)
	private static class MockType {

	}

	private static class Unsupported {

	}
}