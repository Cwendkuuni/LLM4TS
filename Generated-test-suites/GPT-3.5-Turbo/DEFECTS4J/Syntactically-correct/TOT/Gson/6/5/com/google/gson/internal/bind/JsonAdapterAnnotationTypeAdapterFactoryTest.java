
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    private JsonAdapterAnnotationTypeAdapterFactory factory;
    private Gson gson;

    @Before
    public void setUp() {
        factory = new JsonAdapterAnnotationTypeAdapterFactory(new ConstructorConstructor());
        gson = new Gson();
    }

    // create() method test cases

    @Test
    public void testCreate_ReturnsTypeAdapter_WhenAnnotationExists() {
        TypeAdapter<MyModel> typeAdapter = factory.create(gson, TypeToken.get(MyModel.class));
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreate_ReturnsNull_WhenAnnotationDoesNotExist() {
        TypeAdapter<MyModelWithoutAnnotation> typeAdapter = factory.create(gson, TypeToken.get(MyModelWithoutAnnotation.class));
        assertNull(typeAdapter);
    }

    // getTypeAdapter() method test cases

    @Test
    public void testGetTypeAdapter_ReturnsTypeAdapter_WhenValueIsTypeAdapter() {
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(new ConstructorConstructor(),
                gson, TypeToken.get(MyModel.class), new JsonAdapter() {
                    @Override
                    public Class<? extends TypeAdapter<?>> value() {
                        return MyModelTypeAdapter.class;
                    }

                    @Override
                    public Class<?>[] classes() {
                        return new Class<?>[]{MyModel.class};
                    }
                });
        assertNotNull(typeAdapter);
    }

    @Test
    public void testGetTypeAdapter_ReturnsTypeAdapter_WhenValueIsTypeAdapterFactory() {
        TypeAdapter<?> typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(new ConstructorConstructor(),
                gson, TypeToken.get(MyModel.class), new JsonAdapter() {
                    @Override
                    public Class<? extends TypeAdapterFactory> value() {
                        return MyModelTypeAdapterFactory.class;
                    }

                    @Override
                    public Class<?>[] classes() {
                        return new Class<?>[]{MyModel.class};
                    }
                });
        assertNotNull(typeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeAdapter_ThrowsException_WhenValueIsInvalid() {
        JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(new ConstructorConstructor(),
                gson, TypeToken.get(MyModel.class), new JsonAdapter() {
                    @Override
                    public Class<?> value() {
                        return Integer.class; // Invalid value
                    }

                    @Override
                    public Class<?>[] classes() {
                        return new Class<?>[]{MyModel.class};
                    }
                });
    }

    // Complete JUnit test for JsonAdapterAnnotationTypeAdapterFactory

    @Test
    public void testCompleteJsonAdapterAnnotationTypeAdapterFactory() {
        // Test typical use cases
        TypeAdapter<MyModel> typeAdapter = factory.create(gson, TypeToken.get(MyModel.class));
        assertNotNull(typeAdapter);
        assertEquals(new MyModel(), typeAdapter.fromJson("{\"name\":\"John\"}"));
        assertEquals("{\"name\":\"John\"}", typeAdapter.toJson(new MyModel()));

        // Test edge cases
        TypeAdapter<MyModel> nullTypeAdapter = factory.create(gson, TypeToken.get(null));
        assertNull(nullTypeAdapter);
        TypeAdapter<MyModel> nonExistentAdapter = factory.create(gson, TypeToken.get(NonExistentModel.class));
        assertNull(nonExistentAdapter);

        // Test error scenarios
        try {
            TypeAdapter<MyModel> invalidTypeAdapter = factory.create(gson, TypeToken.get(InvalidModel.class));
            fail("Expected JsonParseException to be thrown");
        } catch (JsonParseException e) {
            // Test passed
        }
    }

    // Test models for testing

    public static class MyModel {
        private String name;

        public MyModel() {
            this.name = "John";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonAdapter(MyModelTypeAdapter.class)
    public static class MyModelWithoutAnnotation {
        private String name;

        public MyModelWithoutAnnotation() {
            this.name = "John";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class NonExistentModel {
        // Test class without an adapter
    }

    public static class InvalidModel {
        // Test class with invalid adapter value
        @JsonAdapter(Integer.class)
        private String name;

        public InvalidModel() {
            this.name = "John";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class MyModelTypeAdapter extends TypeAdapter<MyModel> {

        @Override
        public void write(JsonWriter out, MyModel value) throws IOException {
            out.beginObject();
            out.name("name").value(value.getName());
            out.endObject();
        }

        @Override
        public MyModel read(JsonReader in) throws IOException {
            MyModel model = new MyModel();
            in.beginObject();
            while (in.hasNext()) {
                String name = in.nextName();
                if (name.equals("name")) {
                    model.setName(in.nextString());
                } else {
                    in.skipValue();
                }
            }
            in.endObject();
            return model;
        }
    }

    public static class MyModelTypeAdapterFactory implements TypeAdapterFactory {

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            if (MyModel.class.isAssignableFrom(type.getRawType())) {
                return (TypeAdapter<T>) new MyModelTypeAdapter().nullSafe();
            }
            return null;
        }
    }
}
