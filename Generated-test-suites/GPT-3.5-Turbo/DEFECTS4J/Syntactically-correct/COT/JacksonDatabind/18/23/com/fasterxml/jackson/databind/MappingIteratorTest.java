import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.databind.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Exception exception = null;
        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testRemove() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Exception exception = null;
        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testClose() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        iterator.close();
        Assert.assertNull(iterator.getParser());
    }

    @Test
    public void testHasNextValue() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Assert.assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Exception exception = null;
        try {
            iterator.nextValue();
        } catch (NoSuchElementException e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }

    @Test
    public void testReadAll() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Assert.assertEquals(0, iterator.readAll().size());
    }

    @Test
    public void testReadAllWithResultList() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        List<String> result = new ArrayList<>();
        iterator.readAll(result);
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testReadAllWithResults() throws Exception {
        MappingIterator<String> iterator = new MappingIterator<String>(null, null, null, null, false, null);
        Set<String> result = new HashSet<>();
        iterator.readAll(result);
        Assert.assertEquals(0, result.size());
    }

}