import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOptionBuilder;
import org.apache.commons.cli2.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.PatternBuilder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;


public class PatternBuilderTest {
  
  @Test
  public void testCreate() {
    Option option1 = new Option();
    option1.setShortName("a");
    
    Option option2 = new Option();
    option2.setShortName("b");
    
    Set options = new HashSet();
    options.add(option1);
    options.add(option2);
    
    GroupBuilder gbuilder = new GroupBuilder();
    gbuilder.reset();
    for (Iterator i = options.iterator(); i.hasNext();) {
      gbuilder.withOption((Option)i.next());
    }
    Option expected = gbuilder.create();
    
    PatternBuilder patternBuilder = new PatternBuilder();
    patternBuilder.reset();
    patternBuilder.options = options;
    Option actual = patternBuilder.create();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testReset() {
    Set options = new HashSet();
    options.add(new Option());
    options.add(new Option());
    
    PatternBuilder patternBuilder = new PatternBuilder();
    patternBuilder.options = options;
    patternBuilder.reset();
    
    assertTrue(patternBuilder.options.isEmpty());
  }
  
  @Test
  public void testWithPattern() {
    PatternBuilder patternBuilder1 = new PatternBuilder();
    patternBuilder1.reset();
    
    DefaultOptionBuilder obuilder1 = new DefaultOptionBuilder();
    ArgumentBuilder abuilder1 = new ArgumentBuilder();
    
    PatternBuilder patternBuilder2 = new PatternBuilder(new GroupBuilder(), obuilder1, abuilder1);
    patternBuilder2.reset();
    
    assertEquals(patternBuilder1.getClass(), patternBuilder2.getClass());
  }
  
  @Test
  public void testCreateOption() {
    Option option = new Option();
    
    PatternBuilder patternBuilder = new PatternBuilder();
    
    patternBuilder.createOption('o', true, 'x');
    Option actual = (Option)patternBuilder.options.iterator().next();
    
    assertEquals(option.getShortName().charAt(0), actual.getShortName().charAt(0));
  }
  
  @Test
  public void testValidator(){
    Validator expected = new ClassValidator();
    expected.setInstance(true);
    PatternBuilder patternBuilder = new PatternBuilder();
    Validator actual = patternBuilder.validator('@');
    
    assertEquals(expected.getClass(), actual.getClass());
  }
}