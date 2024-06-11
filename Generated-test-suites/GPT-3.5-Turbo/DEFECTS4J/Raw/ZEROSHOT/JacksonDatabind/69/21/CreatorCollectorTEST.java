import org.junit.Assert;
import org.junit.Test;

public class CreatorCollectorTest {

   @Test
   public void testConstructValueInstantiator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       ValueInstantiator result = creatorCollector.constructValueInstantiator(null);
       Assert.assertNotNull(result);
   }

   @Test
   public void testSetDefaultCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.setDefaultCreator(null);
       Assert.assertTrue(creatorCollector.hasDefaultCreator());
   }

   @Test
   public void testAddStringCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addStringCreator(null, false);
   }

   @Test
   public void testAddIntCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addIntCreator(null, false);
   }

   @Test
   public void testAddLongCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addLongCreator(null, false);
   }

   @Test
   public void testAddDoubleCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addDoubleCreator(null, false);
   }

   @Test
   public void testAddBooleanCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addBooleanCreator(null, false);
   }

   @Test
   public void testAddDelegatingCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addDelegatingCreator(null, false, null);
       Assert.assertTrue(creatorCollector.hasDelegatingCreator());
   }

   @Test
   public void testAddPropertyCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addPropertyCreator(null, false, null);
       Assert.assertTrue(creatorCollector.hasPropertyBasedCreator());
   }

   @Test
   public void testAddIncompeteParameter() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       creatorCollector.addIncompeteParameter(null);
   }

   @Test
   public void testHasDefaultCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       boolean result = creatorCollector.hasDefaultCreator();
       Assert.assertFalse(result);
   }

   @Test
   public void testHasDelegatingCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       boolean result = creatorCollector.hasDelegatingCreator();
       Assert.assertFalse(result);
   }

   @Test
   public void testHasPropertyBasedCreator() {
       CreatorCollector creatorCollector = new CreatorCollector(null, null);
       boolean result = creatorCollector.hasPropertyBasedCreator();
       Assert.assertFalse(result);
   }

}