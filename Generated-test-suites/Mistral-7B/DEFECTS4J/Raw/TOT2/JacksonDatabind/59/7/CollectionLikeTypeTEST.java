import org.junit.Assert;
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testGetContentType() {
        JavaType expectedType = new SimpleType("java.util.ArrayList", TypeFactory.defaultInstance());
        CollectionLikeType collectionLikeType = new CollectionLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, expectedType);
        JavaType actualType = collectionLikeType.getContentType();
        Assert.assertEquals(expectedType, actualType);
    }
}