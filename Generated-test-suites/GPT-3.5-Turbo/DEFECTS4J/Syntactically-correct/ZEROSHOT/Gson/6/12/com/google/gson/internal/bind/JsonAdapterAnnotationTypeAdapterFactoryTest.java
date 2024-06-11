import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.*;
import java.lang.reflect.Constructor;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory typeAdapterFactory;
    private Gson gson;

    @Before
    public void setUp() {
        typeAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        gson = new Gson();
    }

    @Test
    public void testCreate_NullAnnotation_ReturnsNull() {
        TypeToken<String> targetType = TypeToken.get(String.class);
        TypeAdapter<String> adapter = typeAdapterFactory.create(gson, targetType);
        assertNull(adapter);
    }

    @Test
    public void testCreate_TypeAdapterAnnotation_ReturnsTypeAdapter() {
        TypeToken<TestClass1> targetType = TypeToken.get(TestClass1.class);
        TypeAdapter<TestClass1> adapter = typeAdapterFactory.create(gson, targetType);
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test
    public void testCreate_TypeAdapterFactoryAnnotation_ReturnsTypeAdapter() {
        TypeToken<TestClass2> targetType = TypeToken.get(TestClass2.class);
        TypeAdapter<TestClass2> adapter = typeAdapterFactory.create(gson, targetType);
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }
            @Override
            public Class<?> value() {
                return null;
            }
        };
        TypeToken<TestClass1> fieldType = TypeToken.get(TestClass1.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterValue_ReturnsTypeAdapter() {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }
            @Override
            public Class<?> value() {
                return TestTypeAdapter.class;
            }
        };
        TypeToken<TestClass1> fieldType = TypeToken.get(TestClass1.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
        assertTrue(adapter instanceof TestTypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryValue_ReturnsTypeAdapter() {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }
            @Override
            public Class<?> value() {
                return TestTypeAdapterFactory.class;
            }
        };
        TypeToken<TestClass2> fieldType = TypeToken.get(TestClass2.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
        assertTrue(adapter instanceof TestTypeAdapter);
    }

    @Test
    public void testGetTypeAdapter_TypeAdapterFactoryNullSafe_ReturnsTypeAdapter() {
        JsonAdapter annotation = new JsonAdapter() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonAdapter.class;
            }
            @Override
            public Class<?> value() {
                return TestTypeAdapterFactory.class;
            }
        };
        TypeToken<TestClass2> fieldType = TypeToken.get(TestClass2.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        TypeAdapter<?> adapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);
        assertNotNull(adapter);
        assertTrue(adapter instanceof TypeAdapter);
        assertTrue(adapter instanceof TestTypeAdapter);
        assertTrue(adapter.isLenient());
        assertTrue(adapter instanceof NullSafeTypeAdapter);
    }

    public static class TestClass1 {
        private String name;
        public TestClass1(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public static class TestClass2 {
        private String name;
        public TestClass2(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    public static class TestTypeAdapter extends TypeAdapter<TestClass1> {
        @Override
        public void write(JsonWriter out, TestClass1 value) throws IOException {
            out.value(value.getName());
        }
        @Override
        public TestClass1 read(JsonReader in) throws IOException {
            return new TestClass1(in.nextString());
        }
    }

    public static class TestTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
            return null;
        }
    }

}