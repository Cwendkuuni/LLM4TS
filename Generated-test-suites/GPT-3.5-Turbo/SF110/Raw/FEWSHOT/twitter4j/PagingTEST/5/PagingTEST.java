import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList());
        
        paging.setSinceId(123456789);
        assertEquals(1, paging.asPostParameterList().size());
        
        paging.setPage(1);
        assertEquals(2, paging.asPostParameterList().size());
        
        paging.setCount(100);
        assertEquals(3, paging.asPostParameterList().size());
        
        paging.setMaxId(987654321);
        assertEquals(4, paging.asPostParameterList().size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray());
        
        paging.setSinceId(123456789);
        assertEquals(1, paging.asPostParameterArray().length);
        
        paging.setPage(1);
        assertEquals(2, paging.asPostParameterArray().length);
        
        paging.setCount(100);
        assertEquals(3, paging.asPostParameterArray().length);
        
        paging.setMaxId(987654321);
        assertEquals(4, paging.asPostParameterArray().length);
    }
    
    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList(Paging.SMCP));
        
        paging.setSinceId(123456789);
        assertEquals(1, paging.asPostParameterList(Paging.SMCP).size());
        
        paging.setPage(1);
        assertEquals(2, paging.asPostParameterList(Paging.SMCP).size());
        
        paging.setCount(100);
        assertEquals(3, paging.asPostParameterList(Paging.SMCP).size());
        
        paging.setMaxId(987654321);
        assertEquals(4, paging.asPostParameterList(Paging.SMCP).size());
    }
    
    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray(Paging.SMCP));
        
        paging.setSinceId(123456789);
        assertEquals(1, paging.asPostParameterArray(Paging.SMCP).length);
        
        paging.setPage(1);
        assertEquals(2, paging.asPostParameterArray(Paging.SMCP).length);
        
        paging.setCount(100);
        assertEquals(3, paging.asPostParameterArray(Paging.SMCP).length);
        
        paging.setMaxId(987654321);
        assertEquals(4, paging.asPostParameterArray(Paging.SMCP).length);
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        
        paging.setPage(10);
        assertEquals(10, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(0);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(100);
        assertEquals(100, paging.getCount());
        
        paging.setCount(200);
        assertEquals(200, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(123456789);
        assertEquals(123456789, paging.getSinceId());
        
        paging.setSinceId(987654321);
        assertEquals(987654321, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(0);
    }
    
    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(123456789);
        assertEquals(123456789, paging.getMaxId());
        
        paging.setMaxId(987654321);
        assertEquals(987654321, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(0);
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 100, 123456789, 987654321);
        Paging paging2 = new Paging(1, 100, 123456789, 987654321);
        assertEquals(true, paging1.equals(paging2));
        
        Paging paging3 = new Paging(2, 200, 123456789, 987654321);
        assertEquals(false, paging1.equals(paging3));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 100, 123456789, 987654321);
        Paging paging2 = new Paging(1, 100, 123456789, 987654321);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        
        Paging paging3 = new Paging(2, 200, 123456789, 987654321);
        assertEquals(false, paging1.hashCode() == paging3.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 100, 123456789, 987654321);
        assertEquals("Paging{page=1, count=100, sinceId=123456789, maxId=987654321}", paging.toString());
    }
}