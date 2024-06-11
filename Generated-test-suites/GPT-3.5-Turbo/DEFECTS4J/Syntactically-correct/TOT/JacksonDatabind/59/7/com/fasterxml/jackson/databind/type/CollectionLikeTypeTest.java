import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType elementType = JavaType.construct(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(List.class, elementType);

        Assert.assertEquals(List.class, collectionLikeType.getRawClass());
        Assert.assertEquals(elementType, collectionLikeType.getContentType());
        Assert.assertFalse(collectionLikeType.isTrueCollectionType());
    }
    
    @Test
    public void testWithTypeHandler() {
        JavaType elementType = JavaType.construct(Integer.class);
        Object typeHandler = new Object();
        CollectionLikeType collectionLikeType = new CollectionLikeType(List.class, null, null, null,
                elementType, null, null, false).withTypeHandler(typeHandler);

        Assert.assertEquals(typeHandler, collectionLikeType.getTypeHandler());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType elementType = JavaType.construct(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, elementType);

        Assert.assertTrue(collectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testToString() {
        JavaType elementType = JavaType.construct(Integer.class);
        CollectionLikeType collectionLikeType = CollectionLikeType.construct(ArrayList.class, elementType);

        String expected = "[collection-like type; class java.util.ArrayList, contains " + elementType + "]";
        Assert.assertEquals(expected, collectionLikeType.toString());
    }

}