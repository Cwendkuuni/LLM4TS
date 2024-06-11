
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PagingTest {
    
    private Paging paging;
    
    @Before
    public void setUp() {
        paging = new Paging();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageConstructor() {
        paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        paging = new Paging(12345L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageAndCountConstructor() {
        paging = new Paging(5, 10);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageAndSinceIdConstructor() {
        paging = new Paging(5, 12345L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        paging = new Paging(5, 10, 12345L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        paging = new Paging(5, 10, 12345L, 67890L);
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(67890L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageConstructor() {
        paging = new Paging(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountConstructor() {
        paging = new Paging(5, -10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdConstructor() {
        paging = new Paging(-12345L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdConstructor() {
        paging = new Paging(5, 10, 12345L, -67890L);
    }
    
    @Test
    public void testGetPage() {
        assertEquals(-1, paging.getPage());
    }
    
    @Test
    public void testSetPage() {
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        paging.setCount(-10);
    }
    
    @Test
    public void testCount() {
        paging.count(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        paging.setSinceId(12345L);
        assertEquals(12345L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        paging.setSinceId(-12345L);
    }
    
    @Test
    public void testSinceId() {
        paging.sinceId(12345L);
        assertEquals(12345L, paging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        paging.setMaxId(67890L);
        assertEquals(67890L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        paging.setMaxId(-67890L);
    }
    
    @Test
    public void testMaxId() {
        paging.maxId(67890L);
        assertEquals(67890L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging2 = new Paging();
        assertTrue(paging.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * (31 * (31 * (31 * 1 - 1) + 1) + 1) + 1;
        assertEquals(expectedHashCode, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        String expectedToString = "Paging{page=-1, count=-1, sinceId=-1, maxId=-1}";
        assertEquals(expectedToString, paging.toString());
    }
    
    @Test
    public void testAsPostParameterList() {
        assertNull(paging.asPostParameterList());
    }
    
    @Test
    public void testAsPostParameterArray() {
        assertEquals(0, paging.asPostParameterArray().length);
    }
    
    @Test
    public void testAsPostParameterListCharStream() {
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList(Paging.SMCP));
    }
    
    @Test
    public void testAsPostParameterArrayCharStream() {
        assertEquals(0, paging.asPostParameterArray(Paging.SMCP).length);
    }
    
    @Test
    public void testAsPostParameterListCharStreamString() {
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList(Paging.SMCP, "count"));
    }
    
    @Test
    public void testAsPostParameterArrayCharStreamString() {
        assertEquals(0, paging.asPostParameterArray(Paging.SMCP, "count").length);
    }
    
    @Test
    public void testAddPostParameter() {
        // TODO: Implement test cases for addPostParameter method
    }
}
