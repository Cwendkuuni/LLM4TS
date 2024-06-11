import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testCreateAdapter() {
        Gson gson = new GsonBuilder().create();
        TypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingPolicy(),
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                }
        );

        TypeAdapter<MyClass> adapter = factory.create(gson, TypeToken.get(MyClass.class));
        assertNotNull(adapter);
    }

    @Test
    public void testExcludeField() {
        Class<?> clazz = MyClassWithExcludedField.class;
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingPolicy(),
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field field, boolean serialize) {
                        return field.getName().equals("excludedField");
                    }
                }
        );

        TypeAdapter<MyClassWithExcludedField> adapter = factory.create(gson, TypeToken.get(clazz));
        MyClassWithExcludedField instance = new MyClassWithExcludedField();
        String json = gson.toJson(instance);
        assertTrue(json.contains("\"name\":\"name1\",\"age\":1"));
        assertTrue(json.contains("\"excludedField\":null"));

        MyClassWithExcludedField deserializedInstance = gson.fromJson(json, clazz);
        assertEquals(instance.getName(), deserializedInstance.getName());
        assertEquals(instance.getAge(), deserializedInstance.getAge());
        assertNull(deserializedInstance.getExcludedField());
    }

    @Test(expected = JsonSyntaxException.class)
    public void testThrowJsonSyntaxException() {
        Class<?> clazz = MyClassWithNullField.class;
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingPolicy(),
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                }
        );

        TypeAdapter<MyClassWithNullField> adapter = factory.create(gson, TypeToken.get(clazz));
        MyClassWithNullField instance = new MyClassWithNullField();
        instance.setNullField(null);

        String json = gson.toJson(instance);

        adapter.write(new JsonWriter(System.out), instance);
    }

    @Test
    public void testWriteField() {
        Class<?> clazz = MyClassWithWriteField.class;
        Gson gson = new GsonBuilder().create();
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(
                new ConstructorConstructor(),
                new FieldNamingPolicy(),
                new Excluder() {
                    @Override
                    public boolean excludeClass(Class<?> clazz, boolean serialize) {
                        return false;
                    }

                    @Override
                    public boolean excludeField(Field field, boolean serialize) {
                        return false;
                    }
                }
        );

        TypeAdapter<MyClassWithWriteField> adapter = factory.create(gson, TypeToken.get(clazz));
        MyClassWithWriteField instance = new MyClassWithWriteField();
        instance.setFieldToBeWritten(new MyNestedClass());

        String json = gson.toJson(instance);
        assertEquals("{\"fieldToBeWritten\":{\"name1\":\"value1\",\"name2\":\"value2\"}}", json);

        adapter.write(new JsonWriter(System.out), instance);
    }

    static class MyClassWithExcludedField {
        @SerializedName("name1")
        private String name;
        private int age;
        @SerializedName("excludedField")
        private String excludedField;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getExcludedField() {
            return excludedField;
        }

        public void setExcludedField(String excludedField) {
            this.excludedField = excludedField;
        }
    }

    static class MyClassWithNullField {
        private MyNestedClass nullField;

        public MyNestedClass getNullField() {
            return nullField;
        }

        public void setNullField(MyNestedClass nullField) {
            this.nullField = nullField;
        }
    }

    static class MyClassWithWriteField {
        private MyNestedClass fieldToBeWritten;

        public MyNestedClass getFieldToBeWritten() {
            return fieldToBeWritten;
        }

        public void setFieldToBeWritten(MyNestedClass fieldToBeWritten) {
            this.fieldToBeWritten = fieldToBeWritten;
        }

        static class MyNestedClass {
            private String name1 = "value1";
            private String name2 = "value2";

            public String getName1() {
                return name1;
            }

            public String getName2() {
                return name2;
            }
        }
    }
}