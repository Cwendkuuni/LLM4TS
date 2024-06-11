package org.jsoup.select.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.jsoup.helper.TestCaseHEAD;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import org.jsoup.select.And;
import org.jsoup.select.Or;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CombiningEvaluatorTest {

 private CombiningEvaluator evaluator;
 private Element root;
 private Element node;

 public CombiningEvaluatorTest(String evaluatorType, String html, String selector, boolean expectedResult) {
 this.evaluatorType = evaluatorType;
 this.html = html;
 this.selector = selector;
 this.expectedResult = expectedResult;
 }

 @Parameterized.Parameters(name = "{index}: {0} evaluator tests with html={1} and selector={2} => expectedResult={3}")
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 {"And", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class1, div.class2", true},
 {"And", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2'></div></body></html>",
 "div.class1, div.class2", false},
 {"And", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class3, div.class2", false},
 {"Or", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class1, div.class2", true},
 {"Or", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class3, div.class2", false},
 {"Or", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class1, div.class3", true},
 {"Or", "<html><head></head><body><div id='div1' class='class1'></div><div id='div2' class='class2'></div></body></html>",
 "div.class2, div.class3", false}
 });
 }

 @Before
 public void setup() {
 root = TestCaseHEAD.getDocument().body();
 node = Mockito.spy(TestCaseHEAD.parse(this.html).body().child(0));
 evaluator = createEvaluator(this.evaluatorType, this.selector.split(", "));
 }

 @Test
 public void testMatches() {
 boolean result = evaluator.matches(root, node);
 assertTrue("Matcher did not return expected result: ", expectedResult == result);
 }

 @Test
 public void testToString() {
 String result = evaluator.toString();
 String expected = StringUtil.join(selector.split(", "), " > ");
 assertTrue("ToString did not return expected result: ", expected.equals(result));
 }

 private CombiningEvaluator createEvaluator(String evaluatorType, String... selectors) {
 if (evaluatorType.equals("And")) {
 return new And(Arrays.asList(Arrays.stream(selectors).map(s -> new ElementSelector(s)).collect(Collectors.toList())));
 } else {
 Or or = new Or();
 Arrays.stream(selectors).forEach(s -> or.add(new ElementSelector(s)));
 return or;
 }
 }

 private static class ElementSelector implements Evaluator {
 private final String selector;

 ElementSelector(String selector) {
 this.selector = selector;
 }

 @Override
 public boolean matches(Element root, Element node) {
 return node.tagName().equals(selector.substring(1));
 }
}

}