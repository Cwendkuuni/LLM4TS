import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MP3Test {

    private MP3 mp3;

    @BeforeEach
    public void setUp() {
        mp3 = new MP3("test.mp3");
    }

    @Test
    public void testRun_NormalFile_SuccessfullyPlays() {
        // Arrange
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        mp3.run();

        // Assert
        assertEquals("Expected output message", outContent.toString());
    }

    @Test
    public void testRun_NullFilename_ExceptionThrown() {
        // Arrange
        mp3 = new MP3(null);

        // Act & Assert
        assertThrows(NullPointerException.class, mp3::run);
    }

    @Test
    public void testRun_EmptyFilename_ExceptionThrown() {
        // Arrange
        mp3 = new MP3("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, mp3::run);
    }

    @Test
    public void testRun_NonexistentFile_ExceptionThrown() {
        // Arrange
        mp3 = new MP3("nonexistent.mp3");

        // Act & Assert
        assertThrows(IOException.class, mp3::run);
    }

    @Test
    public void testRun_AudioSystemError_ExceptionThrown() {
        // Arrange
        mockAudioSystemError();

        // Act & Assert
        assertThrows(Exception.class, mp3::run);
    }

    @Test
    public void testRun_DatLineInfoError_ExceptionThrown() {
        // Arrange
        mockDataLineInfoError();

        // Act & Assert
        assertThrows(Exception.class, mp3::run);
    }

    private void mockAudioSystemError() {
        MP3 mp3 = new MP3("test.mp3") {
            @Override
            protected AudioInputStream getAudioInputStream(File file) throws IOException {
                throw new IOException();
            }
        };
    }

    private void mockDataLineInfoError() {
        MP3 mp3 = new MP3("test.mp3") {
            @Override
            protected DataLine.Info getDataLineInfo(Class<?> arg0, AudioFormat arg1) {
                throw new IllegalArgumentException();
            }
        };
    }
}