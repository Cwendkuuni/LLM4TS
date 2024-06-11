import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Element;
import org.jsoup.helper.ElementFilter;
import org.jsoup.helper.StringUtil;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombiningEvaluatorTest {

 private CombiningEvaluator andEvaluator;
 private CombiningEvaluator orEvaluator;

 @Before
 public void setUp() {
 andEvaluator = new And( Arrays.asList(
 new ElementFilter.NameStartsWith("div"),
 new ElementFilter.TagName("p")
 ) );

 orEvaluator = new Or(
 new And( Arrays.asList(
 new ElementFilter.NameStartsWith("div"),
 new ElementFilter.TagName("p")
 ) ),
 new ElementFilter.TagName("ul")
 );
 }

 @Test
 public void testAndMethod() {
 Element root = Element.parse("<html><body>" +
 "<div class=\"first\">This is a div</div>" +
 "<p>This is a paragraph</p>" +
 "<ul>" +
 "<li>List item</li>" +
 "</ul>" +
 "</body></html>");

 Element node = root.selectFirst(andEvaluator.toString());

 assertTrue(node != null);
 assertTrue(node.tagName().equals("div"));
 assertTrue(node.className().startsWith("first"));
 }

 @Test(expected = IllegalStateException.class)
 public void testAndMethodWithInvalidArgument() {
 andEvaluator = new And(new Object() {});

 try {
 Element node = root.selectFirst(andEvaluator.toString());
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalStateException, but got " + e.getClass().getName());
 }
 }

 @Test
 public void testOrMethod() {
 Element node = root.selectFirst(orEvaluator.toString());

 assertTrue(node != null);
 assertTrue(node.tagName().equals("div"));
 // The first clause matches, so any other clause is not checked

 ElementFilter.TagName listTag = new ElementFilter.TagName("ul");
 orEvaluator.add(listTag);

 node = root.selectFirst(orEvaluator.toString());

 assertTrue(node != null);
 assertTrue(node.tagName().equals("ul"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOrMethodWithInvalidArgument() {
 orEvaluator = new Or(new Object() {});

 try {
 Element node = root.selectFirst(orEvaluator.toString());
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException, but got " + e.getClass().getName());
 }
 }
}