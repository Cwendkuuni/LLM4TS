import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class AbrirTest {

    @Test
    public void testConstructor1() {
        ProcessarEntidades mockForm = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(mockForm, "test.jaw.xml");

        assertEquals("", abrir.xml); // Initial value of xml should be empty string
        assertSame(mockForm, abrir.form); // form object should be same as mockForm passed in constructor
        // Verify that abrirXML method is called when the file has .jaw.xml extension
        Mockito.verify(mockForm).setNomeArquivoSalvo("test.jaw.xml");
        Mockito.verify(mockForm).setEntidades(Mockito.any()); // Verify that setEntidades method is called with any argument
    }

    @Test
    public void testConstructor2() {
        ProcessarEntidades mockForm = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(mockForm, 20);

        assertEquals("", abrir.xml); // Initial value of xml should be empty string
        assertSame(mockForm, abrir.form); // form object should be same as mockForm passed in constructor
        // Verify that open dialog is shown and the file is selected
        // Verify that abrirSerializado method is called when tipoAbrir is 20
        Mockito.verify(mockForm).setNomeArquivoSalvo(Mockito.anyString()); // Verify that setNomeArquivoSalvo method is called with any string argument
        Mockito.verify(mockForm).setEntidades(Mockito.any()); // Verify that setEntidades method is called with any argument
    }

    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades mockForm = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = Mockito.spy(new Abrir(mockForm, "test.jaw"));
        
        abrir.abrirSerializado("test.jaw");

        Mockito.verify(mockForm).setNomeArquivoSalvo("test.jaw"); // Verify that setNomeArquivoSalvo method is called with "test.jaw" argument
        Mockito.verify(mockForm).setEntidades(Mockito.any()); // Verify that setEntidades method is called with any argument

        // Test when file ends with .jaw but not a valid .jaw file
        Mockito.reset(mockForm); // Reset the mockForm object for next test
        abrir.abrirSerializado("invalid.jaw");

        Mockito.verify(mockForm, Mockito.never()).setNomeArquivoSalvo(Mockito.anyString()); // Verify that setNomeArquivoSalvo method is not called
        Mockito.verify(mockForm, Mockito.never()).setEntidades(Mockito.any()); // Verify that setEntidades method did not called
    }

    @Test
    public void testAbrirXML() {
        ProcessarEntidades mockForm = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = Mockito.spy(new Abrir(mockForm, "test.jaw.xml"));
        
        abrir.abrirXML("test.jaw.xml");

        Mockito.verify(mockForm).setNomeArquivoSalvo("test.jaw.xml"); // Verify that setNomeArquivoSalvo method is called with "test.jaw.xml" argument
        Mockito.verify(mockForm).setEntidades(Mockito.any()); // Verify that setEntidades method is called with any argument

        // Test when file ends with .jaw.xml but not a valid .jaw.xml file
        Mockito.reset(mockForm); // Reset the mockForm object for next test
        abrir.abrirXML("invalid.jaw.xml");

        Mockito.verify(mockForm, Mockito.never()).setNomeArquivoSalvo(Mockito.anyString()); // Verify that setNomeArquivoSalvo method is not called
        Mockito.verify(mockForm, Mockito.never()).setEntidades(Mockito.any()); // Verify that setEntidades method did not called
    }
}