import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    // Test cases for method 1 - Constructor with arguments (CollectionType)
    // ...

    // Test cases for Method 2 - Deprecated constructor with one argument (CollectionType)
    // ...

    // Test cases for Method 3 - withContentType method
    // ...

    // Edge cases for constructor with null values
    @Test
    public void testCollectionTypeConstructorWithNullValues() {
        CollectionType collectionType = new CollectionType(null, null);
        assertNull(collectionType.getRawClass());
        assertNull(collectionType.getBindings());
    }

    // Edge case: constructor with invalid argument types
    @Test(expected = IllegalArgumentException.class)
    public void testCollectionTypeConstructorWithInvalidArguments() {
        CollectionType collectionType = new CollectionType("String", null);
    }

    // Edge case: withContentType method with null argument
    @Test(expected = NullPointerException.class)
    public void testWithContentTypeMethodWithNullArgument() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null);
        collectionType.withContentType(null);
    }

    // ... Add more test cases for other methods if needed
}