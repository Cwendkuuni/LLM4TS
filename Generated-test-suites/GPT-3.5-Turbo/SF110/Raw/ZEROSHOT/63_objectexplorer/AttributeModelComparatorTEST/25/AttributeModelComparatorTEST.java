import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_FirstIsNull_ReturnNegativeOne() {
        AttributeModel second = new AttributeModel("Test");
        int result = comparator.compare(null, second);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondIsNull_ReturnOne() {
        AttributeModel first = new AttributeModel("Test");
        int result = comparator.compare(first, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothAreNull_ReturnZero() {
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_BothAreSame_ReturnZero() {
        AttributeModel model = new AttributeModel("Test");
        int result = comparator.compare(model, model);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_SameName_ReturnZero() {
        AttributeModel first = new AttributeModel("Test");
        AttributeModel second = new AttributeModel("TEST");
        int result = comparator.compare(first, second);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNullSecondEqualName_ReturnNegativeOne() {
        AttributeModel second = new AttributeModel("Test");
        int result = comparator.compare(null, second);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstEqualNameSecondNull_ReturnOne() {
        AttributeModel first = new AttributeModel("Test");
        int result = comparator.compare(first, null);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstLessThanSecond_ReturnNegativeOne() {
        AttributeModel first = new AttributeModel("abc");
        AttributeModel second = new AttributeModel("def");
        int result = comparator.compare(first, second);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstGreaterThanSecond_ReturnOne() {
        AttributeModel first = new AttributeModel("def");
        AttributeModel second = new AttributeModel("abc");
        int result = comparator.compare(first, second);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothStartsWith_A_ReturnOne() {
        AttributeModel first = new AttributeModel("abc");
        AttributeModel second = new AttributeModel("abd");
        int result = comparator.compare(first, second);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_FirstStartsWith_A_ReturnNegativeOne() {
        AttributeModel first = new AttributeModel("abc");
        AttributeModel second = new AttributeModel("def");
        int result = comparator.compare(first, second);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondStartsWith_A_ReturnOne() {
        AttributeModel first = new AttributeModel("def");
        AttributeModel second = new AttributeModel("abc");
        int result = comparator.compare(first, second);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_NeitherStartsWith_A_ReturnZero() {
        AttributeModel first = new AttributeModel("bcd");
        AttributeModel second = new AttributeModel("def");
        int result = comparator.compare(first, second);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_CaseSensitiveComparison() {
        AttributeModel first = new AttributeModel("abc");
        AttributeModel second = new AttributeModel("ABD");
        int result = comparator.compare(first, second);
        assertTrue(result < 0);
    }
}