import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.FieldNamingStrategy;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws IOException {
        // Given
        FieldNamingStrategy fieldNamingPolicy = mock(FieldNamingStrategy.class);
        Excluder excluder = mock(Excluder.class);
        ConstructorConstructor constructorConstructor = mock(ConstructorConstructor.class);
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(fieldNamingPolicy)
                .setExcluder(excluder)
                .setConstructorConstructor(constructorConstructor)
                .create();

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder);

        // When & Then
        TypeToken<MyClass> typeToken = new TypeToken<MyClass>() {};
        TypeAdapter<MyClass> adapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        Assert.assertNotNull(adapter);

        MyClass obj = new MyClass();
        obj.setName("Test");
        obj.setNumber(123);
        obj.setList(new ArrayList<>());
        obj.getList().add(new MyInnerClass());
        obj.getList().add(new MyInnerClass());

        JsonWriter jsonWriter = mock(JsonWriter.class);
        adapter.write(jsonWriter, obj);
        String json = jsonWriter.toString();

        JsonReader jsonReader = mock(JsonReader.class);
        when(jsonReader.nextString()).thenReturn("Test");
        when(jsonReader.nextInt()).thenReturn(123);
        when(jsonReader.beginArray()).thenReturn(true);
        MyClass objFromJson = adapter.read(jsonReader);

        Assert.assertEquals(obj, objFromJson);
    }

    private static class MyClass {
        @SerializedName("name")
        private String name;
        @JsonAdapter(MyInnerClassAdapter.class)
        private List<MyInnerClass> list;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<MyInnerClass> getList() {
            return list;
        }

        public void setList(List<MyInnerClass> list) {
            this.list = list;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    private static class MyInnerClass {}

    private static class MyInnerClassAdapter extends TypeAdapter<MyInnerClass> {
        @Override
        public MyInnerClass read(JsonReader jsonReader) throws IOException {
            return new MyInnerClass();
        }

        @Override
        public void write(JsonWriter jsonWriter, MyInnerClass value) throws IOException {
            jsonWriter.beginObject();
            jsonWriter.name("innerClass");
            jsonWriter.value(value.getClass().getName());
            jsonWriter.endObject();
        }
    }
}