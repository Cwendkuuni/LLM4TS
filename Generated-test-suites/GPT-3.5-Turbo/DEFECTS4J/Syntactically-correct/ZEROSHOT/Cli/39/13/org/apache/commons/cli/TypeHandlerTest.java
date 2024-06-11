import org.junit.Test;
import org.junit.Assert;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringShouldReturnString() throws ParseException {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        Assert.assertEquals(str, result);
    }
    
    @Test
    public void testCreateValueWithObjectShouldReturnObject() throws ParseException {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        Assert.assertEquals(str, result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithNumberShouldReturnNumber() throws ParseException {
        String str = "10";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(Long.valueOf(str), result);
    }
    
    @Test
    public void testCreateValueWithDecimalNumberShouldReturnDouble() throws ParseException {
        String str = "10.55";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        Assert.assertEquals(Double.valueOf(str), result);
    }
    
    @Test
    public void testCreateValueWithDateShouldReturnDate() throws ParseException {
        String str = "2021-01-01";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Date);
    }
    
    @Test
    public void testCreateValueWithClassShouldReturnClass() throws ParseException {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        Assert.assertNotNull(result);
        Assert.assertEquals(str, result.getClass().getName());
    }
    
    @Test
    public void testCreateValueWithFileShouldReturnFile() throws ParseException {
        String str = "test.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(str, ((File) result).getName());
    }
    
    @Test
    public void testCreateValueWithExistingFileShouldReturnFile() throws ParseException {
        String str = "test.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(str, ((File) result).getName());
    }
    
    @Test
    public void testCreateValueWithFilesShouldReturnArrayOfFiles() throws ParseException {
        String str = "file1.txt,file2.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File[]);
        File[] files = (File[]) result;
        Assert.assertEquals(2, files.length);
        Assert.assertEquals("file1.txt", files[0].getName());
        Assert.assertEquals("file2.txt", files[1].getName());
    }
    
    @Test
    public void testCreateValueWithURLShouldReturnURL() throws ParseException {
        String str = "http://www.example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(str, result.toString());
    }
    
    @Test
    public void testCreateValueWithInvalidTypeShouldReturnNull() throws ParseException {
        String str = "invalid";
        Object result = TypeHandler.createValue(str, Integer.class);
        Assert.assertNull(result);
    }
    
    @Test
    public void testCreateObjectShouldReturnNewInstance() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        Assert.assertNotNull(result);
        Assert.assertEquals(classname, result.getClass().getName());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassShouldThrowParseException() throws ParseException {
        String classname = "invalid";
        TypeHandler.createObject(classname);
    }
    
    @Test
    public void testCreateNumberWithIntegerShouldReturnLong() throws ParseException {
        String str = "10";
        Object result = TypeHandler.createNumber(str);
        Assert.assertEquals(Long.valueOf(str), result);
    }
    
    @Test
    public void testCreateNumberWithDecimalShouldReturnDouble() throws ParseException {
        String str = "10.55";
        Object result = TypeHandler.createNumber(str);
        Assert.assertEquals(Double.valueOf(str), result);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidNumberShouldThrowParseException() throws ParseException {
        String str = "invalid";
        TypeHandler.createNumber(str);
    }
    
    @Test
    public void testCreateClassShouldReturnClass() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createClass(classname);
        Assert.assertNotNull(result);
        Assert.assertEquals(classname, result.getName());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassShouldThrowParseException() throws ParseException {
        String classname = "invalid";
        TypeHandler.createClass(classname);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateShouldThrowUnsupportedOperationException() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }
    
    @Test
    public void testCreateURLShouldReturnURL() throws ParseException {
        String str = "http://www.example.com";
        Object result = TypeHandler.createURL(str);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof URL);
        Assert.assertEquals(str, result.toString());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURLShouldThrowParseException() throws ParseException {
        String str = "invalid";
        TypeHandler.createURL(str);
    }
    
    @Test
    public void testCreateFileShouldReturnFile() {
        String str = "test.txt";
        Object result = TypeHandler.createFile(str);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof File);
        Assert.assertEquals(str, ((File) result).getName());
    }
    
    @Test
    public void testCreateFilesShouldThrowUnsupportedOperationException() {
        String str = "file1.txt,file2.txt";
        try {
            TypeHandler.createFiles(str);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            Assert.assertEquals("Not yet implemented", e.getMessage());
        }
    }

}