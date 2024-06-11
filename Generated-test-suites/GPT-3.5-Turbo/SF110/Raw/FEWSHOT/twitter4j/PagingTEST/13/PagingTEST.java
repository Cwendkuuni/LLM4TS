import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;

public class PagingTest {
    
    @Test
    public void testConstructor1() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor2() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor3() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor4() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor5() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor6() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor7() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }
    
    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        
        paging.setPage(10);
        assertEquals(10, paging.getPage());
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
        
        paging.setCount(1);
        assertEquals(1, paging.getCount());
        
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
        
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
        
        paging.setSinceId(100L);
        assertEquals(100L, paging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
        
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());
        
        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertTrue(paging1.equals(paging2));
        
        Paging paging3 = new Paging(2, 10, 1L, 100L);
        assertTrue(!paging1.equals(paging3));
        
        Paging paging4 = new Paging(1, 11, 1L, 100L);
        assertTrue(!paging1.equals(paging4));
        
        Paging paging5 = new Paging(1, 10, 2L, 100L);
        assertTrue(!paging1.equals(paging5));
        
        Paging paging6 = new Paging(1, 10, 1L, 101L);
        assertTrue(!paging1.equals(paging6));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        
        Paging paging3 = new Paging(2, 10, 1L, 100L);
        assertTrue(paging1.hashCode() != paging3.hashCode());
        
        Paging paging4 = new Paging(1, 11, 1L, 100L);
        assertTrue(paging1.hashCode() != paging4.hashCode());
        
        Paging paging5 = new Paging(1, 10, 2L, 100L);
        assertTrue(paging1.hashCode() != paging5.hashCode());
        
        Paging paging6 = new Paging(1, 10, 1L, 101L);
        assertTrue(paging1.hashCode() != paging6.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals("Paging{page=1, count=10, sinceId=1, maxId=100}", paging.toString());
    }

    @Test
    public void testAsPostParameterList1() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        List<HttpParameter> params = paging.asPostParameterList();
        List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();
        expectedParams.add(new HttpParameter("since_id", "1"));
        expectedParams.add(new HttpParameter("max_id", "100"));
        expectedParams.add(new HttpParameter("per_page", "10"));
        expectedParams.add(new HttpParameter("page", "1"));
        assertEquals(expectedParams, params);
    }
    
    @Test
    public void testAsPostParameterList2() {
        Paging paging = new Paging(1);
        List<HttpParameter> params = paging.asPostParameterList(Paging.SMCP, "count");
        List<HttpParameter> expectedParams = new ArrayList<HttpParameter>();
        expectedParams.add(new HttpParameter("since_id", "-1"));
        expectedParams.add(new HttpParameter("max_id", "-1"));
        expectedParams.add(new HttpParameter("count", "-1"));
        expectedParams.add(new HttpParameter("page", "1"));
        assertEquals(expectedParams, params);
    }
    
    @Test
    public void testAsPostParameterArray1() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        HttpParameter[] params = paging.asPostParameterArray();
        HttpParameter[] expectedParams = {
            new HttpParameter("since_id", "1"),
            new HttpParameter("max_id", "100"),
            new HttpParameter("per_page", "10"),
            new HttpParameter("page", "1")
        };
        assertEquals(expectedParams.length, params.length);
        for (int i = 0; i < params.length; i++) {
            assertTrue(params[i].equals(expectedParams[i]));
        }
    }
    
    @Test
    public void testAsPostParameterArray2() {
        Paging paging = new Paging(1);
        HttpParameter[] params = paging.asPostParameterArray(Paging.SMCP, "count");
        HttpParameter[] expectedParams = {
            new HttpParameter("since_id", "-1"),
            new HttpParameter("max_id", "-1"),
            new HttpParameter("count", "-1"),
            new HttpParameter("page", "1")
        };
        assertEquals(expectedParams.length, params.length);
        for (int i = 0; i < params.length; i++) {
            assertTrue(params[i].equals(expectedParams[i]));
        }
    }
}