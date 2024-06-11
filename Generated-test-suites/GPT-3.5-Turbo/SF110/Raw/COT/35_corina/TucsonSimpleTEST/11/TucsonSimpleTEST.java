import org.junit.Test;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.load(new BufferedReader(new StringReader("")));
    }

    @Test
    public void testMake6digitCode_withIdInMeta() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCode_withoutIdInMeta() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testMake6digitCode_withIdLengthGreaterThan6() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.meta.put("id", "1234567890");
        assertEquals("123456", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader_yIsBeforeStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(1999);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 6, y);
        assertEquals("000000 1999", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsAfterStart() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2001);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 6, y);
        assertEquals("123456 2001", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsStart_whenIsProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2000);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals(" 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsBeforeStartAndProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(1999);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals(" 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsBeforeStartAndProcessed_1() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(1999);
        writer.write("123456 ");
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("123456 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsBeforeStartAndProcessed_2() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(1999);
        writer.write("123456 ");
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("123456 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsBeforeStartAndProcessed_3() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(1999);
        writer.write("123");
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("123 2000", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsAfterEndAndNotProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2021);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 6, y);
        assertEquals("999   ", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsAfterEndAndProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2021);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("9990   ", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsInRangeAndNotProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2001);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 6, y);
        assertEquals("1234562001", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsInRangeAndProcessed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2001);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("12342001", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsInRangeAndProcessed_1() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2019);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("12342019", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsInRangeAndProcessed_2() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2019);
        writer.write("1234 ");
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("1234 2019", writer.toString());
    }

    @Test
    public void testSaveRowHeader_yIsBeforeEndAndProcessed_whenIsSummed() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        Year y = new Year(2020);
        tucsonSimple.saveRowHeader(writer, sample, tucsonSimple.make6digitCode(sample), 4, y);
        assertEquals("999020", writer.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        sample.data.add(1.0);
        tucsonSimple.saveData(sample, writer);
        assertEquals("000000 2000  1.000\n", writer.toString());
    }

    @Test
    public void testComputeRange_whenStartIsBefore1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(-1), new Year(20));
        assertEquals(new Range(new Year(1001), new Year(1001).add(8000)), tucsonSimple.computeRange(sample));
    }

    @Test
    public void testComputeRange_whenStartPlus8000IsBefore1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(7999), new Year(10000));
        assertEquals(new Range(new Year(1001), new Year(10001)), tucsonSimple.computeRange(sample));
    }

    @Test
    public void testComputeRange_whenStartIsAfter1() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        assertEquals(new Range(new Year(2000), new Year(2020)), tucsonSimple.computeRange(sample));
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = new BufferedReader(new StringReader(""));
        BufferedWriter writer = new BufferedWriter(new StringWriter());
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2020));
        tucsonSimple.save(sample, writer);

        String expectedOutput = 
        "000000 2000\n" +
        "0000002000\n" +
        "000000 2001\n" +
        "0000001999\n" +
        "000000 2002\n" +
        "0000001998\n" +
        "000000 2003\n" +
        "0000001997\n" +
        "000000 2004\n" +
        "0000001996\n" +
        "000000 2005\n" +
        "0000001995\n" +
        "000000 2006\n" +
        "0000001994\n" +
        "000000 2007\n" +
        "0000001993\n" +
        "000000 2008\n" +
        "0000001992\n" +
        "000000 2009\n" +
        "0000001991\n" +
        "000000 2010\n" +
        "0000001990\n" +
        "000000 2011\n" +
        "0000001989\n" +
        "000000 2012\n" +
        "0000001988\n" +
        "000000 2013\n" +
        "0000001987\n" +
        "000000 2014\n" +
        "0000001986\n" +
        "000000 2015\n" +
        "0000001985\n" +
        "000000 2016\n" +
        "0000001984\n" +
        "000000 2017\n" +
        "0000001983\n" +
        "000000 2018\n" +
        "0000001982\n" +
        "000000 2019\n" +
        "0000001981\n" +
        "   999\n" +
        "9990   ";

        assertEquals(expectedOutput, writer.toString());
    }
}