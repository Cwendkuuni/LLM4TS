import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_WhenCountryIsNull_ShouldReturnEmptyGeoUsAddress() {
        // Arrange
        GeoAddress addr = new GeoAddress();
        addr.setAddressDetails(new AddressDetails());
        
        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        
        // Assert
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testExecute_WhenCountryNameCodeListIsEmpty_ShouldReturnEmptyGeoUsAddress() {
        // Arrange
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        country.setCountryNameCode(new ArrayList<>());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        
        // Assert
        assertEquals("", result.getCountry());
    }
    
    @Test
    public void testExecute_WhenCountryNameCodeListNotEmpty_ShouldSetFirstCountryNameToGeoUsAddress() {
        // Arrange
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("USA");
        countryNames.add(countryNameCode);
        country.setCountryNameCode(countryNames);
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        
        // Assert
        assertEquals("USA", result.getCountry());
    }
    
    @Test
    public void testExecute_WhenAdministrativeAreaIsNull_ShouldNotSetStateToGeoUsAddress() {
        // Arrange
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = AddressToUsAddressFunctor.getInstance().execute(addr);
        
        // Assert
        assertNull(result.getState());
    }
    
    // Additional test methods for AdministrativeArea, SubAdministrativeArea, Locality, PostalCode, Thoroughfare, etc.
    
}