package twitter4j;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> postParamList = paging.asPostParameterList();
        assertEquals(1, postParamList.size());
        assertEquals("count", postParamList.get(0).getName());
        assertEquals("-1", postParamList.get(0).getValue());
        
        paging = new Paging(1);
        postParamList = paging.asPostParameterList();
        assertEquals(2, postParamList.size());
        assertEquals("count", postParamList.get(0).getName());
        assertEquals("1", postParamList.get(0).getValue());
        assertEquals("page", postParamList.get(1).getName());
        assertEquals("1", postParamList.get(1).getValue());
        
        paging = new Paging(1, 10);
        postParamList = paging.asPostParameterList();
        assertEquals(3, postParamList.size());
        assertEquals("count", postParamList.get(0).getName());
        assertEquals("10", postParamList.get(0).getValue()); 
        assertEquals("page", postParamList.get(1).getName());
        assertEquals("1", postParamList.get(1).getValue());
        assertEquals("since_id", postParamList.get(2).getName());
        assertEquals("-1", postParamList.get(2).getValue());
        
        paging = new Paging(1, 10, 123456789);
        postParamList = paging.asPostParameterList();
        assertEquals(4, postParamList.size());
        assertEquals("count", postParamList.get(0).getName());
        assertEquals("10", postParamList.get(0).getValue()); 
        assertEquals("page", postParamList.get(1).getName());
        assertEquals("1", postParamList.get(1).getValue());
        assertEquals("since_id", postParamList.get(2).getName());
        assertEquals("123456789", postParamList.get(2).getValue());
        assertEquals("max_id", postParamList.get(3).getName());
        assertEquals("-1", postParamList.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] postParamArray = paging.asPostParameterArray();
        assertEquals(1, postParamArray.length);
        assertEquals("count", postParamArray[0].getName());
        assertEquals("-1", postParamArray[0].getValue());
        
        paging = new Paging(1);
        postParamArray = paging.asPostParameterArray();
        assertEquals(2, postParamArray.length);
        assertEquals("count", postParamArray[0].getName());
        assertEquals("1", postParamArray[0].getValue());
        assertEquals("page", postParamArray[1].getName());
        assertEquals("1", postParamArray[1].getValue());
        
        paging = new Paging(1, 10);
        postParamArray = paging.asPostParameterArray();
        assertEquals(3, postParamArray.length);
        assertEquals("count", postParamArray[0].getName());
        assertEquals("10", postParamArray[0].getValue());
        assertEquals("page", postParamArray[1].getName());
        assertEquals("1", postParamArray[1].getValue());
        assertEquals("since_id", postParamArray[2].getName());
        assertEquals("-1", postParamArray[2].getValue());
        
        paging = new Paging(1, 10, 123456789);
        postParamArray = paging.asPostParameterArray();
        assertEquals(4, postParamArray.length);
        assertEquals("count", postParamArray[0].getName());
        assertEquals("10", postParamArray[0].getValue());
        assertEquals("page", postParamArray[1].getName());
        assertEquals("1", postParamArray[1].getValue());
        assertEquals("since_id", postParamArray[2].getName());
        assertEquals("123456789", postParamArray[2].getValue());
        assertEquals("max_id", postParamArray[3].getName());
        assertEquals("-1", postParamArray[3].getValue());
    }
    
    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 123456789);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("123456789", pagingParams.get(0).getValue());
        
        paging = new Paging();
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", -1);
        assertEquals(0, pagingParams.size());
        
        paging = new Paging();
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 0);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("0", pagingParams.get(0).getValue());
        
        paging = new Paging();
        pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 1);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id", pagingParams.get(0).getName());
        assertEquals("1", pagingParams.get(0).getValue());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 123456789, 987654321);
        Paging paging2 = new Paging(1, 10, 123456789, 987654321);
        Paging paging3 = new Paging(1, 20, 123456789, 987654321);
        
        assertEquals(paging1, paging2);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertEquals(paging1.toString(), paging2.toString());
        
        assertEquals(paging1, paging1);
        assertEquals(paging1.hashCode(), paging1.hashCode());
        assertEquals(paging1.toString(), paging1.toString());
        
        assertEquals(paging1.equals(paging2) && paging2.equals(paging3), paging1.equals(paging3));
    }

}