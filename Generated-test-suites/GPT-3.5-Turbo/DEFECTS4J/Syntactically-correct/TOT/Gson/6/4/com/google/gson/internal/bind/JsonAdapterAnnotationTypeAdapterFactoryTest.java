import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class JsonAdapterAnnotationTypeAdapterFactoryTest {

    @Test
    public void create_JsonAdapterAnnotationExist_ReturnsTypeAdapter() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<TestObject> targetType = TypeToken.get(TestObject.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<TestObject> result = factory.create(gson, targetType);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void create_NoJsonAdapterAnnotation_ReturnsNull() {
        // Arrange
        Gson gson = new Gson();
        TypeToken<NoAnnotationObject> targetType = TypeToken.get(NoAnnotationObject.class);
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<NoAnnotationObject> result = factory.create(gson, targetType);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_TypeAdapterAnnotation_ReturnsTypeAdapterInstance() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
    }

    @Test
    public void getTypeAdapter_TypeAdapterFactoryAnnotation_ReturnsTypeAdapterInstance() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObjectFactory.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeAdapter_InvalidAnnotationValue_ThrowsIllegalArgumentException() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = InvalidAnnotation.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert (Expected Exception)
    }

    @Test
    public void getTypeAdapter_NonNullSafeTypeAdapter_ReturnsNullSafeTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObjectNonNullable.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
        assertTrue(result.isLenient());
    }

    /* Additional Test Cases */
    
    @Test
    public void create_ConstructorConstructorIsNull_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = null;
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.create(gson, fieldType);

        // Assert
        assertNull(result);
    }

    @Test
    public void create_GsonIsNull_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = null;
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.create(gson, fieldType);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_FieldTypeIsNull_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = null;
        JsonAdapter annotation = TestObject.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_AnnotationIsNull_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = null;
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_InvalidTypeAdapterClass_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObjectInvalid.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_InvalidTypeAdapterFactoryClass_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = InvalidAdapterFactory.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_InvalidAnnotationClass_ReturnsNull() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = InvalidAnnotationClass.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getTypeAdapter_ValidAnnotationValue_ReturnsNullSafeTypeAdapter() {
        // Arrange
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        Gson gson = new Gson();
        TypeToken<TestObject> fieldType = TypeToken.get(TestObject.class);
        JsonAdapter annotation = TestObjectValid.class.getAnnotation(JsonAdapter.class);
        JsonAdapterAnnotationTypeAdapterFactory factory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

        // Act
        TypeAdapter<?> result = factory.getTypeAdapter(constructorConstructor, gson, fieldType, annotation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TypeAdapter);
        assertNotNull(((TypeAdapter) result).nullSafe());
    }
}