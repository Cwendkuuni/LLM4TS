import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testCreateExtendedMessageFormat() {
        String pattern = "Hello {0, choice, 0#Hello{1}, 1#Hi{1}}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0, choice, 0#Hello{1}, 1#Hi{1}}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateExtendedMessageFormatInvalidPattern() {
        String pattern = "Hello {0, choice, 0#Hello{1}, 1#Hi{1} {2}}";
        new ExtendedMessageFormat(pattern);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0, choice, 0#Hello{1}, 1#Hi{1}}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0, choice, 0#Hello{1}, 1#Hi{1}}");
        assertEquals("Hello {0, choice, 0#Hello{1}, 1#Hi{1}}", format.toPattern());

        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("CustomFormat", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
                        fail("Should not be called");
                        return null;
                    }

                    @Override
                    public String parameterName() {
                        return null;
                    }

                    @Override
                    public Class<? extends ParsePosition> getParsePositionType() {
                        return ParsePosition.class;
                    }
                };
            }
        });
        format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern("Hello {0, choice, 0#Hello{1}, 1#Hi{1}}");
        assertEquals("Hello {0, choice, 0#Hello{1}, 1#Hi{1}}", format.toPattern());

        format.applyPattern("Hello {0, choice, 0#CustomFormat{1}, 1#Hi{1}}");
        assertEquals("Hello {0, choice, 0#CustomFormat{1}, 1#Hi{1}}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidFormatName() {
        String pattern = "Hello {0, choice, 0#CustomFormat{1}, 1#Hi{1}}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        format.applyPattern("Hello {0, choice, 0#CustomFormat{1}, 1#Hi{1}}");
    }

    @Test
    public void testApplyPatternWithFormatFactory() {
        String pattern = "Hello {0, choice, 0#CustomFormatA{1}, 1#CustomFormatB{1}}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null);
        Map<String, FormatFactory> registry = new java.util.HashMap<>();
        registry.put("CustomFormatA", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
                        return "CustomFormatA: " + object.toString();
                    }

                    @Override
                    public String parameterName() {
                        return null;
                    }

                    @Override
                    public Class<? extends ParsePosition> getParsePositionType() {
                        return ParsePosition.class;
                    }
                };
            }
        });
        registry.put("CustomFormatB", new FormatFactory() {
            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                return new Format() {
                    @Override
                    public String format(Object object, Locale locale, String format, ParsePosition parsePosition) {
                        return "CustomFormatB: " + object.toString();
                    }

                    @Override
                    public String parameterName() {
                        return null;
                    }

                    @Override
                    public Class<? extends ParsePosition> getParsePositionType() {
                        return ParsePosition.class;
                    }
                };
            }
        });
        format.applyPattern("Hello {0, choice, 0#CustomFormatA{1}, 1#CustomFormatB{1}}");
        String formatPattern = format.toPattern();
        assertEquals("Hello {0, choice, 0#CustomFormatA{1}, 1#CustomFormatB{1}}", formatPattern);

        Object[] args = new Object[]{"World"};
        String formatted = format.format(args);
        String expected = "Hello CustomFormatA: World, CustomFormatB: World";
        assertEquals(expected, formatted);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", null);
        format.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", null);
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", null);
        Format[] formats = new Format[1];
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}", null);
        Format[] formats = new Format[1];
        format.setFormatsByArgumentIndex(formats);
    }

}