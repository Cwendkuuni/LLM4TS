import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
   
   @Test
   public void testDetach() {
      Component component = new Component();
      SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
      
      helper.detach();
      
      assertNull(helper.component);
   }
   
   @Test
   public void testNotifyListener() {
      Component component = new Component();
      SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
      SimpleKeyListener listener = new SimpleKeyListener() {
         public void enter(Component component) {}
         public void insert(Component component) {}
         public void delete(Component component) {}
         public void tab(Component component) {}
      };
      
      helper.notifyListener(listener, 10, null);
      
      // Assert listener.enter() is called
      
      helper.notifyListener(listener, 155, null);
      
      // Assert listener.insert() is called
      
      helper.notifyListener(listener, 127, null);
      
      // Assert listener.delete() is called
      
      helper.notifyListener(listener, 9, null);
      
      // Assert listener.tab() is called
   }
   
   @Test
   public void testKeyPressed() {
      Component component = new Component();
      SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(component);
      KeyEvent event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);
      
      helper.keyPressed(event);
      
      // Assert fire() method is called with KeyEvent.VK_TAB
      
      event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
      
      helper.keyPressed(event);
      
      // Assert fire() method is called with KeyEvent.VK_ENTER
      
      event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);
      
      helper.keyPressed(event);
      
      // Assert fire() method is called with KeyEvent.VK_DELETE
      
      event = new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);
      
      helper.keyPressed(event);
      
      // Assert fire() method is called with KeyEvent.VK_INSERT
   }
   
   @Test
   public void testKeyReleased() {
      // No actions, empty method
   }
   
   @Test
   public void testKeyTyped() {
      // No actions, empty method
   }
}