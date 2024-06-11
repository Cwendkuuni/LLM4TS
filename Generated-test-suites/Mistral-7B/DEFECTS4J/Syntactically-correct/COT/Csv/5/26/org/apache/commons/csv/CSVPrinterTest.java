import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;
    private final String[] testValues = {"Test", "123", "true", "\"quoted value\""};

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), new CSVFormat(',', Quote.MINIMAL));
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        for (String value : testValues) {
            printer.print(value);
        }
        printer.println();
        assertEquals(getTestDelimitedString(), outputStream.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        String input = "Test,123,\"true\",\"\"quoted value\"\"";
        printer = new CSVPrinter(new StringWriter(outputStream), new CSVFormat(',', Quote.ALL));
        for (String value : testValues) {
            printer.print(value);
        }
        printer.println();
        assertEquals(input, outputStream.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        String input = "Test,\"Test\",\"123\",\"true\",\"\"quoted value\"\"";
        printer = new CSVPrinter(new StringWriter(outputStream), new CSVFormat(',', Quote.MINIMAL));
        for (int i = 0; i < testValues.length; i++) {
            printer.print(testValues[i]);
            if (i < 2) {
                printer.print(',');
            }
        }
        printer.println();
        assertEquals(input, outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        assertEquals(getTestCommentedString(), outputStream.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        assertEquals(getTestDelimitedString() + "\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(testValues);
        printer.println();
        assertEquals(getTestDelimitedString(), outputStream.toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException, SQLException {
        ResultSet resultSet = getTestResultSet();
        printer.printRecords(resultSet);
        assertEquals(getTestCsvStringFromResultSet(resultSet), outputStream.toString());
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        printer.printRecords(testValues);
        printer.println();
        assertEquals(getTestDelimitedString(), outputStream.toString());
    }

    @Test
    public void testPrintRecordWithObject() throws IOException {
        printer.printRecord(new Object[]{"Test1", "123", "true", "\"quoted value\""});
        printer.println();
        assertEquals(getTestDelimitedString(), outputStream.toString());
    }

    private String getTestDelimitedString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String value : testValues) {
            stringBuilder.append(value).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String getTestCommentedString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("// This is a comment\n");
        for (String value : testValues) {
            stringBuilder.append(value).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String getTestCsvStringFromResultSet(ResultSet resultSet) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            stringBuilder.append(resultSet.getString(i)).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            stringBuilder.append(resultSet.getString(i)).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private ResultSet getTestResultSet() throws SQLException {
        String[] testValues = {"Test1", "123", "true", "\"quoted value\""};
        return getTestResultSet(testValues);
    }

    private ResultSet getTestResultSet(String[] testValues) throws SQLException {
        return new MockResultSet(testValues);
    }

    private static class MockResultSet implements ResultSet {

        private Object[] values;

        public MockResultSet(Object[] values) {
            this.values = values;
        }

        @Override
        public boolean next() throws SQLException {
            return true;
        }

        @Override
        public boolean wasNull() throws SQLException {
            return false;
        }

        @Override
        public int findColumn(String columnName) throws SQLException {
            return 0;
        }

        @Override
        public boolean getBoolean(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Boolean ? ((Boolean) values[columnIndex]).booleanValue() : false;
        }

        @Override
        public byte getByte(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Byte ? ((Byte) values[columnIndex]).byteValue() : 0;
        }

        @Override
        public short getShort(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Short ? ((Short) values[columnIndex]).shortValue() : 0;
        }

        @Override
        public int getInt(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Integer ? ((Integer) values[columnIndex]).intValue() : 0;
        }

        @Override
        public long getLong(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Long ? ((Long) values[columnIndex]).longValue() : 0;
        }

        @Override
        public float getFloat(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Float ? ((Float) values[columnIndex]).floatValue() : 0;
        }

        @Override
        public double getDouble(int columnIndex) throws SQLException {
            return values[columnIndex] instanceof Double ? ((Double) values[columnIndex]).doubleValue() : 0;
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            return values[columnIndex].toString();
        }

        @Override
        public Object getObject(int columnIndex) throws SQLException {
            return values[columnIndex];
        }

        @Override
        public int getRow() throws SQLException {
            return 0;
        }

        @Override
        public ResultSetMetaData getMetaData() throws SQLException {
            return new MockMetaData();
        }

        private static class MockMetaData implements ResultSetMetaData {

            @Override
            public int getColumnCount() throws SQLException {
                return values.length;
            }

            @Override
            public String getColumnLabel(int column) throws SQLException {
                return Integer.toString(column);
            }

            @Override
            public int getColumnType(int column) throws SQLException {
                return java.sql.Types.VARCHAR;
            }

            @Override
            public boolean isAutoIncrement(int column) throws SQLException {
                return false;
            }

            @Override
            public boolean isCaseSensitive(int column) throws SQLException {
                return true;
            }

            @Override
            public boolean isSearchable(int column) throws SQLException {
                return true;
            }

            @Override
            public String getColumnName(int column) throws SQLException {
                return Integer.toString(column);
            }

            @Override
            public boolean isNullable(int column) throws SQLException {
                return true;
            }

            @Override
            public boolean isSigned(int column) throws SQLException {
                return true;
            }

            @Override
            public int getPrecision(int column) throws SQLException {
                return 0;
            }

            @Override
            public int getScale(int column) throws SQLException {
                return 0;
            }

            @Override
            public String getSchemaName(int column) throws SQLException {
                return "";
            }

            @Override
            public String getTableName(int column) throws SQLException {
                return "";
            }

            @Override
            public boolean isReadOnly(int column) throws SQLException {
                return false;
            }

            @Override
            public boolean isWritable(int column) throws SQLException {
                return true;
            }

            @Override
            public boolean isDefinitelyWritable(int column) throws SQLException {
                return true;
            }
        }
    }
}