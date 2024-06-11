import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedMessageFormatTest {

 private static class DummyFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String args, Locale locale) {
 return new DummyFormat();
 }
 }

 @Test
 public void testEmptyPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("");
 assertEquals("", fmt.toPattern());
 }

 @Test
 public void testBasicPattern() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
 assertEquals("{0} {1}", fmt.toPattern());
 }

 @Test
 public void testFormatFactory() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,date,dd-MM-yyyy} {1,choice,0#Hello|1#Goodbye}", Locale.getDefault(), new DummyFormatFactory());
 assertEquals("{0,date,dd-MM-yyyy} {1,choice,0#Hello|1#Goodbye}", fmt.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBadFormatDescription() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,foo}", Locale.getDefault());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingEndFe() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
 fmt.applyPattern("{0} {1} {2}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingStartFe() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0} {1}", Locale.getDefault());
 fmt.applyPattern("{1} {0}");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyFormatName() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,} {1}", Locale.getDefault());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyFormatStyle() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0,} {1,} {2}", Locale.getDefault());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.getDefault());
 fmt.setFormat(0, new DummyFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.getDefault());
 fmt.setFormatByArgumentIndex(0, new DummyFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.getDefault());
 Format[] formats = new DummyFormat[1];
 fmt.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat fmt = new ExtendedMessageFormat("{0}", Locale.getDefault());
 Format[] formats = new DummyFormat[1];
 fmt.setFormatsByArgumentIndex(new int[]{0}, formats);
 }

 private static class DummyFormat implements Format {
 @Override
 public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
 return "";
 }

 @Override
 public Object parseObject(String source, ParsePosition parsePosition) {
 return null;
 }
 }

}