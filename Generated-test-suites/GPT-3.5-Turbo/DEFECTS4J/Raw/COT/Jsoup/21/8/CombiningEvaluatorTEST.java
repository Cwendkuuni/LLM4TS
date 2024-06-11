import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class CombiningEvaluatorTest {

   @Test
   public void testAndMatches() {
       Element root = new Element("root");
       Element node = new Element("node");

       CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               },
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               }
       );

       assertTrue(andEvaluator.matches(root, node));
   }
   
   @Test
   public void testAndNotMatches() {
       Element root = new Element("root");
       Element node = new Element("node");

       CombiningEvaluator.And andEvaluator = new CombiningEvaluator.And(
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               },
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return false;
                   }
               }
       );

       assertFalse(andEvaluator.matches(root, node));
   }

   @Test
   public void testOrMatches() {
       Element root = new Element("root");
       Element node = new Element("node");

       CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               },
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               }
       );

       assertTrue(orEvaluator.matches(root, node));
   }
   
   @Test
   public void testOrNotMatches() {
       Element root = new Element("root");
       Element node = new Element("node");

       CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return false;
                   }
               },
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return false;
                   }
               }
       );

       assertFalse(orEvaluator.matches(root, node));
   }
   
   @Test
   public void testOrMixedMatches() {
       Element root = new Element("root");
       Element node = new Element("node");

       CombiningEvaluator.Or orEvaluator = new CombiningEvaluator.Or(
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return false;
                   }
               },
               new Evaluator() {
                   @Override
                   public boolean matches(Element root, Element node) {
                       return true;
                   }
               }
       );

       assertTrue(orEvaluator.matches(root, node));
   }

}