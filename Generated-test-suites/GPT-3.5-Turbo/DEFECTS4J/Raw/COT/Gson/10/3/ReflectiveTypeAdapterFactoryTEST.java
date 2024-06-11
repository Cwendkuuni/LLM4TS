
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Field f = getField("excludeField");
        boolean result = reflectiveTypeAdapterFactory.excludeField(f, true);
        Assert.assertFalse(result);
    }

    private Field getField(String fieldName) {
        try {
            return ReflectiveTypeAdapterFactory.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testGetFieldNames() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Field f = getField("getFieldNames");
        List<String> result = reflectiveTypeAdapterFactory.getFieldNames(f);
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testCreate() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        TypeAdapter<TestObject> result = reflectiveTypeAdapterFactory.create(gson, type);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateBoundField() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        Field field = getField("createBoundField");
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField result = reflectiveTypeAdapterFactory.createBoundField(gson, field, "testField", type, serialize, deserialize);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetBoundFields() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        Class<?> raw = type.getRawType();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> result = reflectiveTypeAdapterFactory.getBoundFields(gson, type, raw);
        Assert.assertNotNull(result);
    }

    @Test
    public void testBoundFieldWriteField() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        Field field = getField("read");
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "testField", type, serialize, deserialize);
        try {
            boolean result = boundField.writeField(type);
            Assert.assertFalse(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBoundFieldWrite() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        Field field = getField("write");
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "testField", type, serialize, deserialize);
        try {
            boundField.write(null, new TestObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBoundFieldRead() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        }, new Excluder());
        Gson gson = new Gson();
        TypeToken<TestObject> type = new TypeToken<TestObject>() {};
        Field field = getField("read");
        boolean serialize = true;
        boolean deserialize = true;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "testField", type, serialize, deserialize);
        try {
            boundField.read(null, new TestObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdapterRead() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        };
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = reflectiveTypeAdapterFactory.new Adapter<TestObject>(constructorConstructor.get(TestObject.class), new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>());
        try {
            adapter.read(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAdapterWrite() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy() {
            @Override
            public String translateName(Field f) {
                return null;
            }
        };
        Excluder excluder = new Excluder();
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        ReflectiveTypeAdapterFactory.Adapter<TestObject> adapter = reflectiveTypeAdapterFactory.new Adapter<TestObject>(constructorConstructor.get(TestObject.class), new LinkedHashMap<String, ReflectiveTypeAdapterFactory.BoundField>());
        try {
            adapter.write(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class TestObject {

    }

}
