import com.google.gson.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testReflectiveTypeAdapterFactory() throws IOException {
        // Given
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setExcluder(Excluder.defaults())
                .create();

        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(gson),
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
                Excluder.defaults());

        MyClass myClass = new MyClass("John Doe", 25, new Address("123 Main St", "Anytown", "12345"));

        // When
        String json = gson.toJson(myClass);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());
        JsonReader reader = new JsonReader(inputStream);

        MyClass deserializedObject = gson.fromJson(reader, MyClass.class);

        // Then
        Assert.assertEquals(myClass, deserializedObject);

        // Test JSON adapter
        MyJsonClass myJsonClass = new MyJsonClass(new MyJsonInnerClass("Inner"));
        Gson jsonAdapterGson = new GsonBuilder()
                .registerTypeAdapterFactory(reflectiveTypeAdapterFactory)
                .create();

        json = jsonAdapterGson.toJson(myJsonClass);
        inputStream = new ByteArrayInputStream(json.getBytes());
        reader = new JsonReader(inputStream);

        MyJsonClass deserializedJsonObject = jsonAdapterGson.fromJson(reader, MyJsonClass.class);

        Assert.assertEquals(myJsonClass, deserializedJsonObject);

        // Test exclusion
        MyExcludedClass myExcludedClass = new MyExcludedClass();
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setExcluder(Excluder.prettyPrintFieldNames(new String[]{"excludedField"}))
                .create();

        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(gson),
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES,
                Excluder.prettyPrintFieldNames(new String[]{"excludedField"}));

        json = gson.toJson(myExcludedClass);
        inputStream = new ByteArrayInputStream(json.getBytes());
        reader = new JsonReader(inputStream);

        MyExcludedClass deserializedExcludedObject = gson.fromJson(reader, MyExcludedClass.class);

        Assert.assertNull(deserializedExcludedObject.excludedField);
    }

    static class MyClass {
        private final String name;
        private final int age;
        private final Address address;

        public MyClass(String name, int age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        @Override
        public String toString() {
            return "MyClass{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address=" + address +
                    '}';
        }

        public static class Address {
            private final String street;
            private final String city;
            private final int zipCode;

            public Address(String street, String city, int zipCode) {
                this.street = street;
                this.city = city;
                this.zipCode = zipCode;
            }

            @Override
            public String toString() {
                return "Address{" +
                        "street='" + street + '\'' +
                        ", city='" + city + '\'' +
                        ", zipCode=" + zipCode +
                        '}';
            }
        }
    }

    static class MyJsonClass {
        private final MyJsonInnerClass inner;

        public MyJsonClass(MyJsonInnerClass inner) {
            this.inner = inner;
        }

        @JsonAdapter(MyJsonInnerClassJsonAdapter.class)
        public MyJsonInnerClass getInner() {
            return inner;
        }

        static class MyJsonInnerClass {
            private final String innerName;

            public MyJsonInnerClass(String innerName) {
                this.innerName = innerName;
            }

            public String getInnerName() {
                return innerName;
            }
        }

        static class MyJsonInnerClassJsonAdapter extends TypeAdapter<MyJsonInnerClass> {
            @Override
            public MyJsonInnerClass read(JsonReader in) throws IOException {
                String innerName = in.nextString();
                return new MyJsonInnerClass(innerName);
            }

            @Override
            public void write(JsonWriter out, MyJsonInnerClass value) throws IOException {
                out.value(value.innerName);
            }
        }
    }

    static class MyExcludedClass {
        private String excludedField;

        public MyExcludedClass() {
        }

        public String getExcludedField() {
            return excludedField;
        }

        public void setExcludedField(String excludedField) {
            this.excludedField = excludedField;
        }
    }
}