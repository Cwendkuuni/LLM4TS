package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.cli.CSVFormat;
import org.apache.commons.cli.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private OutputStream out;

    @Before
    public void setup() {
        format = new CSVFormatBuilder().setDelimiter(';').toDefault();
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(out, format);
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        verify(out).close();
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        verify(out).flush();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("foo");
        printer.flush();
        assertTrue(out.toString().equals("foo"));
    }

    @Test
    public void testPrint_null() throws IOException {
        printer.print(null);
        printer.flush();
        String nullString = format.getNullString();
        assertTrue(nullString == null ? out.toString().isEmpty() : out.toString().equals(nullString));
    }

    @Test
    public void testPrintQuote() throws IOException {
        printer.print("foo\"bar");
        printer.flush();
        assertTrue(out.toString().equals("\"foo\"\"bar\""));
    }

    @Test
    public void testPrintEscape() throws IOException {
        printer.print("foo\\bar");
        printer.flush();
        assertTrue(out.toString().equals("foo\\bar"));
    }

    @Test
    public void testPrintEscapeQuote() throws IOException {
        printer.print("foo\\\"bar");
        printer.flush();
        assertTrue(out.toString().equals("foo\\\"bar"));
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new Object[]{"foo", "bar"});
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecord_null() throws IOException {
        printer.printRecord(new Object[]{null, "bar"});
        printer.flush();
        String nullString = format.getNullString();
        assertTrue(nullString == null ? out.toString().isEmpty() : out.toString().equals(nullString + ";bar"));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        printer.printRecords(values);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("foo");
        when(resultSet.getString(2)).thenReturn("bar");
        printer.printRecords(resultSet);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("foo");
        printer.flush();
        assertTrue(out.toString().startsWith("--\n-- foo\n"));
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        assertTrue(out.toString().equals(format.getRecordSeparator()));
    }

    @Test
    public void testPrintRecord_Array() throws IOException {
        printer.printRecord(new String[]{"foo", "bar"});
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecord_Iterable() throws IOException {
        Iterable<Object> iterable = new Object[]{"foo", "bar"};
        printer.printRecord(iterable);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Collection() throws IOException, SQLException {
        Iterable<Iterable<String>> collection = new Iterable<Iterable<String>>() {
            @Override
            public Iterator<Iterable<String>> iterator() {
                return new Iterator<Iterable<String>>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }
                    @Override
                    public Iterable<String> next() {
                        return new Object[]{values[index++]};
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(collection);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Iterable_Array() throws IOException, SQLException {
        Iterable<Iterable<Object>> iterable = new Iterable<Iterable<Object>>() {
            @Override
            public Iterator<Iterable<Object>> iterator() {
                return new Iterator<Iterable<Object>>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }
                    @Override
                    public Iterable<Object> next() {
                        return new Object[]{values[index++]};
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(iterable);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Iterable_Iterable() throws IOException, SQLException {
        Iterable<Iterable<String>> iterable = new Iterable<Iterable<String>>() {
            @Override
            public Iterator<Iterable<String>> iterator() {
                return new Iterator<Iterable<String>>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }
                    @Override
                    public Iterable<String> next() {
                        return new Object[]{"foo", "bar"};
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(iterable);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Iterable_Object() throws IOException, SQLException {
        Iterable<Iterable<Object>> iterable = new Iterable<Iterable<Object>>() {
            @Override
            public Iterator<Iterable<Object>> iterator() {
                return new Iterator<Iterable<Object>>() {
                    int index = 0;
                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }
                    @Override
                    public Iterable<Object> next() {
                        return new Object[]{values[index++]};
                    }
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(iterable);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Collection_Array() throws IOException, SQLException {
        Collection<Object[]> collection = new ArrayList<>();
        collection.add(new Object[]{"foo", "bar"});
        printer.printRecords(collection.toArray());
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Collection_Iterable() throws IOException, SQLException {
        Collection<Iterable<String>> collection = new ArrayList<>();
        collection.add(Arrays.asList(new Object[]{"foo", "bar"}));
        printer.printRecords(collection.toArray());
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Collection_Iterable_Array() throws IOException, SQLException {
        Collection<Iterable<Object>> collection = new ArrayList<>();
        collection.add(Arrays.asList(new Object[]{"foo", "bar"}));
        printer.printRecords(collection.toArray());
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_Collection_Object() throws IOException, SQLException {
        Collection<Object[]> collection = new ArrayList<>();
        collection.add(new Object[]{"foo", "bar"});
        printer.printRecords(collection.toArray());
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(values[0]);
        when(resultSet.getString(2)).thenReturn(values[1]);
        printer.printRecords(resultSet);
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_ResultSet_Array() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(values[0]);
        when(resultSet.getString(2)).thenReturn(values[1]);
        printer.printRecords(resultSet, new String[]{"foo", "bar"});
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_ResultSet_Iterable() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(values[0]);
        when(resultSet.getString(2)).thenReturn(values[1]);
        printer.printRecords(resultSet, Arrays.asList(new Object[]{"foo", "bar"}));
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_ResultSet_Iterable_Array() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(values[0]);
        when(resultSet.getString(2)).thenReturn(values[1]);
        printer.printRecords(resultSet, Arrays.asList(new Object[]{values[0], values[1]}).toArray());
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }

    @Test
    public void testPrintRecords_ResultSet_Iterable_Object() throws IOException, SQLException {
        String[] values = {"foo", "bar"};
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(values[0]);
        when(resultSet.getString(2)).thenReturn(values[1]);
        printer.printRecords(resultSet, Arrays.asList(new Object[]{values[0], values[1]}));
        printer.flush();
        assertTrue(out.toString().equals("foo;bar"));
    }
}