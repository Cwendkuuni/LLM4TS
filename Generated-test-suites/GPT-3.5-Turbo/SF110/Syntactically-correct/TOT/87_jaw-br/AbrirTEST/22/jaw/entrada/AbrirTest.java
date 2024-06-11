import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class AbrirTest {
    
    @Test
    public void testConstructorWithXML() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.jaw.xml");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        Mockito.verify(form).setNomeArquivoSalvo("file.jaw.xml");
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testConstructorWithSerializado() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.jaw");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        Mockito.verify(form).setNomeArquivoSalvo("file.jaw");
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testConstructorWithABRIR_XML() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        Mockito.verify(form).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testConstructorWithABRIR() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        Mockito.verify(form).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testAbrirSerializadoWithValidFile() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.jaw");

        abrir.abrirSerializado("file.jaw");
        
        Mockito.verify(form).setNomeArquivoSalvo("file.jaw");
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.txt");

        abrir.abrirSerializado("file.txt");
        
        Mockito.verify(form, Mockito.never()).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form, Mockito.never()).setEntidades(Mockito.any());
        Mockito.verifyStatic(JOptionPane.class);
        JOptionPane.showMessageDialog(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(0));
        Main.janelaPrincipal.log(Mockito.anyString());
    }
    
    @Test
    public void testAbrirXMLWithValidFile() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.jaw.xml");

        abrir.abrirXML("file.jaw.xml");
        
        Mockito.verify(form).setNomeArquivoSalvo("file.jaw.xml");
        Mockito.verify(form).setEntidades(Mockito.any());
    }
    
    @Test
    public void testAbrirXMLWithInvalidFile() {
        ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
        Abrir abrir = new Abrir(form, "file.txt");

        abrir.abrirXML("file.txt");
        
        Mockito.verify(form, Mockito.never()).setNomeArquivoSalvo(Mockito.any());
        Mockito.verify(form, Mockito.never()).setEntidades(Mockito.any());
        Mockito.verifyStatic(JOptionPane.class);
        JOptionPane.showMessageDialog(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.eq(0));
        Main.janelaPrincipal.log(Mockito.anyString());
    }
}