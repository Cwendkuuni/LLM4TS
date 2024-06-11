
package geo.google.mapping;

import org.junit.Test;
import static org.junit.Assert.*;

public class AddressToUsAddressFunctorTest {
    
    @Test
    public void execute_shouldReturnGeoUsAddressWithCorrectCountry() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("United States");
        country.getCountryNameCode().add(countryName);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals("United States", result.getCountry());
    }
    
    @Test
    public void execute_shouldReturnGeoUsAddressWithCorrectState() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        AddressDetails.Country.AdministrativeArea.AdministrativeAreaName name = new AddressDetails.Country.AdministrativeArea.AdministrativeAreaName();
        name.setContent("California");
        adminArea.getAdministrativeAreaName().add(name);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals("California", result.getState());
    }
    
    @Test
    public void execute_shouldReturnGeoUsAddressWithCorrectCounty() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subName = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subName.setContent("County Name");
        subAdminArea.getSubAdministrativeAreaName().add(subName);
        adminArea.setSubAdministrativeArea(subAdminArea);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals("County Name", result.getCounty());
    }
    
    @Test
    public void execute_shouldReturnGeoUsAddressWithCorrectCity() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        AddressDetails.Country.AdministrativeArea.Locality locality = new AddressDetails.Country.AdministrativeArea.Locality();
        AddressDetails.Country.AdministrativeArea.Locality.LocalityName cityName = new AddressDetails.Country.AdministrativeArea.Locality.LocalityName();
        cityName.setContent("City Name");
        locality.getLocalityName().add(cityName);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals("City Name", result.getCity());
    }
    
    @Test
    public void execute_shouldReturnGeoUsAddressWithCorrectPostalCode() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.AdministrativeArea adminArea = new AddressDetails.Country.AdministrativeArea();
        AddressDetails.Country.AdministrativeArea.Locality locality = new AddressDetails.Country.AdministrativeArea.Locality();
        AddressDetails.Country.AdministrativeArea.Locality.PostalCode postalCode = new AddressDetails.Country.AdministrativeArea.Locality.PostalCode();
        AddressDetails.Country.AdministrativeArea.Locality.PostalCode.PostalCodeNumber number = new AddressDetails.Country.AdministrativeArea.Locality.PostalCode.PostalCodeNumber();
        number.setContent("12345");
        postalCode.getPostalCodeNumber().add(number);
        locality.setPostalCode(postalCode);
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addr.setAddressDetails(new AddressDetails());
        addr.getAddressDetails().setCountry(country);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals("12345", result.getPostalCode());
    }
    
    // Additional test cases for edge cases and exception handling can be added here
    
}
