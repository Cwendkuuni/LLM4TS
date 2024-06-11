
import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsTrueCollectionType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        boolean result = collectionLikeType.isTrueCollectionType();
        
        assert result;
    }

    @Test
    public void testGetContentType() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        JavaType result = collectionLikeType.getContentType();
        
        assert result != null;
    }

    @Test
    public void testGetContentValueHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        Object result = collectionLikeType.getContentValueHandler();
        
        assert result != null;
    }

    @Test
    public void testGetContentTypeHandler() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        Object result = collectionLikeType.getContentTypeHandler();
        
        assert result != null;
    }

    @Test
    public void testHasHandlers() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        boolean result = collectionLikeType.hasHandlers();
        
        assert !result;
    }

    @Test
    public void testGetErasedSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = collectionLikeType.getErasedSignature(sb);
        
        assert result != null;
    }

    @Test
    public void testGetGenericSignature() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        StringBuilder sb = new StringBuilder();
        StringBuilder result = collectionLikeType.getGenericSignature(sb);
        
        assert result != null;
    }

    @Test
    public void testEquals() {
        CollectionLikeType collectionLikeType1 = new CollectionLikeType();
        CollectionLikeType collectionLikeType2 = new CollectionLikeType();
        
        boolean result = collectionLikeType1.equals(collectionLikeType2);
        
        assert result;
    }

    @Test
    public void testToString() {
        CollectionLikeType collectionLikeType = new CollectionLikeType();
        
        String result = collectionLikeType.toString();
        
        assert result != null;
    }
}
