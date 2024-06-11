package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    private static final String TEST_INPUT = "&amp;&#x26;&#48;&#x61;&#x6C;&#x65;&#x72;&#x74;&#x23;";
    private static final String EXPECTED_OUTPUT = "&amp;&£Faith&";

    @Test
    public void testTranslateNormalCase() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#x26;&#48;&#x61;&#x6C;&#x65;&#x72;&#x74;&#x23;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        assertEquals(EXPECTED_OUTPUT, new String(outputStream.toByteArray(), "UTF-8"));
        assertEquals(TEST_INPUT.length() + 2 + 2 + 1, length);
    }

    @Test
    public void testTranslateInvalidCharacterSequence() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#x26;&#48;&#x61;&#x6C;&#x65;abcd;&#x72;&#x74;&#x23;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode("&amp;&#x26;&#48;&#x61;&#x6C;&#x65;abcd;&#x72;&#x74;&#x23;"), 0, writer);
        });
    }

    @Test
    public void testTranslateInvalidEntityFormat1() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#x26&#48;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        });
    }

    @Test
    public void testTranslateInvalidEntityFormat2() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#x26;#48;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        });
    }

    @Test
    public void testTranslateInvalidEntityFormat3() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#26;#48;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        });
    }

    @Test
    public void testTranslateInvalidEntityFormat4() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;&#x26;#x48;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(NumberFormatException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), 0, writer);
        });
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        int length = unescaper.translate(CharSequenceUtils.quotedPrintableDecode(""), 0, writer);
        assertEquals(0, length);
    }

    @Test
    public void testTranslateNullInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(null, 0, new Writer() {
                @Override
                public void write(int c) throws IOException {
                    // no-op
                }

                @Override
                public void flush() throws IOException {
                    // no-op
                }

                @Override
                public void close() throws IOException {
                    // no-op
                }
            });
        });

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(null), 0, new Writer() {
                @Override
                public void write(int c) throws IOException {
                    // no-op
                }

                @Override
                public void flush() throws IOException {
                    // no-op
                }

                @Override
                public void close() throws IOException {
                    // no-op
                }
            });
        });

        assertThrows(NullPointerException.class, () -> {
            unescaper.translate(null, 0, new Writer() {
                @Override
                public void write(int c) throws IOException {
                    // no-op
                }

                @Override
                public void flush() throws IOException {
                    // no-op
                }

                @Override
                public void close() throws IOException {
                    // no-op
                }
            });
        });
    }

    @Test
    public void testTranslateNegativeInputIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "&amp;&#x26;&#48;&#x61;&#x6C;&#x65;&#x72;&#x74;&#x23;";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                outputStream.write(c);
            }

            @Override
            public void flush() throws IOException {
                // no-op
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), -1, writer);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            unescaper.translate(CharSequenceUtils.quotedPrintableDecode(input), input.length() - 1, writer);
        });
    }

}