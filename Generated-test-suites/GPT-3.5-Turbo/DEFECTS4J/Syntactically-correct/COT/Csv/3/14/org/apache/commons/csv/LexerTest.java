
import org.apache.commons.csv.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new FileReader("test.csv"));
            Lexer lexer = new Lexer(format, reader);
            assertEquals(0, lexer.getLineNumber());
            reader.readLine();
            assertEquals(1, lexer.getLineNumber());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Test
    public void testReadEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("\"\\r\\n\""));
            Lexer lexer = new Lexer(format, reader);
            assertEquals(Lexer.CR, lexer.readEscape());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("example  "));
            Lexer lexer = new Lexer(format, reader);
            StringBuilder buffer = new StringBuilder();
            buffer.append(reader.readLine());
            lexer.trimTrailingSpaces(buffer);
            assertEquals("example", buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testReadEndOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("line1\nline2\r\nline3"));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.readEndOfLine(reader.read()));
            assertTrue(lexer.readEndOfLine(reader.read()));
            assertFalse(lexer.readEndOfLine(reader.read()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader(" "));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isWhitespace(reader.read()));
            assertFalse(lexer.isWhitespace('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("\n"));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isStartOfLine(reader.read()));
            reader.resetLineNumber();
            assertFalse(lexer.isStartOfLine('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader(""));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isEndOfFile(reader.read()));
            assertFalse(lexer.isEndOfFile('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader(","));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isDelimiter(reader.read()));
            assertFalse(lexer.isDelimiter('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsEscape() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("\\"));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isEscape(reader.read()));
            assertFalse(lexer.isEscape('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsQuoteChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("\""));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isQuoteChar(reader.read()));
            assertFalse(lexer.isQuoteChar('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void testIsCommentStart() {
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader reader = null;
        try {
            reader = new ExtendedBufferedReader(new StringReader("#"));
            Lexer lexer = new Lexer(format, reader);
            assertTrue(lexer.isCommentStart(reader.read()));
            assertFalse(lexer.isCommentStart('a'));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
