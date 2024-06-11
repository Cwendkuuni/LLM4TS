import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class ReflectiveTypeAdapterFactoryTest {

	private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
	private Gson gson;
	private ConstructorConstructor constructorConstructor;
	private FieldNamingStrategy fieldNamingStrategy;
	private Excluder excluder;
	
	@Before
	public void setup() {
		constructorConstructor = new ConstructorConstructor();
		fieldNamingStrategy = new FieldNamingStrategy() {
			
			@Override
			public String translateName(Field f) {
				return null;
			}
		};
		excluder = new Excluder();
		reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
		gson = new Gson();
	}
	
	@Test
	public void testExcludeField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		boolean result = reflectiveTypeAdapterFactory.excludeField(f, true);
		assertFalse(result);
		
		f = ReflectiveTypeAdapterFactory.class.getDeclaredField("fieldNamingPolicy");
		result = reflectiveTypeAdapterFactory.excludeField(f, true);
		assertFalse(result);
	}
	
	@Test
	public void testCreate() {
		TypeToken<String> type = TypeToken.get(String.class);
		TypeAdapter<String> adapter = reflectiveTypeAdapterFactory.create(gson, type);
		assertNull(adapter);
	}
	
	@Test
	public void testCreateBoundField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> context = ReflectiveTypeAdapterFactory.class;
		Method method = context.getDeclaredMethod("createBoundField", Gson.class, Field.class, String.class, TypeToken.class, boolean.class, boolean.class);
		method.setAccessible(true);
		
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		Object result = method.invoke(reflectiveTypeAdapterFactory, gson, f, name, fieldType, serialize, deserialize);
		assertNotNull(result);
		assertTrue(result instanceof ReflectiveTypeAdapterFactory.BoundField);
	}
	
	@Test
	public void testGetFieldNames() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		List<String> result = reflectiveTypeAdapterFactory.getFieldNames(f);
		assertNotNull(result);
		assertTrue(result.isEmpty());
		
		f = ReflectiveTypeAdapterFactory.class.getDeclaredField("fieldNamingPolicy");
		result = reflectiveTypeAdapterFactory.getFieldNames(f);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testCreate() {
		TypeToken<String> type = TypeToken.get(String.class);
		TypeAdapter<String> adapter = reflectiveTypeAdapterFactory.create(gson, type);
		assertNull(adapter);
	}
	
	@Test
	public void testCreateBoundField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> context = ReflectiveTypeAdapterFactory.class;
		Method method = context.getDeclaredMethod("createBoundField", Gson.class, Field.class, String.class, TypeToken.class, boolean.class, boolean.class);
		method.setAccessible(true);
		
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		Object result = method.invoke(reflectiveTypeAdapterFactory, gson, f, name, fieldType, serialize, deserialize);
		assertNotNull(result);
		assertTrue(result instanceof ReflectiveTypeAdapterFactory.BoundField);
	}
	
	@Test
	public void testGetBoundFields() {
		TypeToken<String> type = TypeToken.get(String.class);
		Class<?> raw = type.getRawType();
		Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testBoundFieldWriteField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> context = ReflectiveTypeAdapterFactory.BoundField.class;
		Method method = context.getDeclaredMethod("writeField", Object.class);
		method.setAccessible(true);
		
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, f, name, fieldType, serialize, deserialize);
		
		boolean result = (boolean) method.invoke(boundField, new Object());
		assertFalse(result);
	}
	
	@Test
	public void testBoundFieldWrite() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> context = ReflectiveTypeAdapterFactory.BoundField.class;
		Method method = context.getDeclaredMethod("write", JsonWriter.class, Object.class);
		method.setAccessible(true);
		
		JsonWriter writer = new JsonWriter(null);
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, f, name, fieldType, serialize, deserialize);
		
		method.invoke(boundField, writer, new Object());
	}
	
	@Test
	public void testBoundFieldRead() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, IOException {
		Class<?> context = ReflectiveTypeAdapterFactory.BoundField.class;
		Method method = context.getDeclaredMethod("read", JsonReader.class, Object.class);
		method.setAccessible(true);
		
		JsonReader reader = new JsonReader(null);
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, f, name, fieldType, serialize, deserialize);
		
		method.invoke(boundField, reader, new Object());
	}
	
	@Test
	public void testBoundFieldWriteField() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Class<?> context = ReflectiveTypeAdapterFactory.BoundField.class;
		Method method = context.getDeclaredMethod("writeField", Object.class);
		method.setAccessible(true);
		
		Field f = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
		String name = "testName";
		TypeToken<?> fieldType = TypeToken.get(String.class);
		boolean serialize = true;
		boolean deserialize = false;
		
		ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, f, name, fieldType, serialize, deserialize);
		
		boolean result = (boolean) method.invoke(boundField, new Object());
		assertFalse(result);
	}
	
	@Test
	public void testAdapterRead() throws IOException {
		JsonReader reader = new JsonReader(null);
		ReflectiveTypeAdapterFactory.Adapter<String> adapter = reflectiveTypeAdapterFactory.new Adapter<>(Object::new, new LinkedHashMap<>());
		String result = adapter.read(reader);
		assertNull(result);
	}
	
	@Test
	public void testAdapterWrite() throws IOException {
		JsonWriter writer = new JsonWriter(null);
		ReflectiveTypeAdapterFactory.Adapter<String> adapter = reflectiveTypeAdapterFactory.new Adapter<>(Object::new, new LinkedHashMap<>());
		adapter.write(writer, null);
	}
}